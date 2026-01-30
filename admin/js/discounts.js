// Discount Management Functions

let allDiscounts = [];

async function renderDiscounts(container) {
    const tpl = document.getElementById('tpl-discounts');
    container.innerHTML = '';
    container.appendChild(tpl.content.cloneNode(true));

    try {
        const response = await fetch(`${CONFIG.API_URL}/discounts`);
        allDiscounts = await response.json();

        calculateDiscountStats(allDiscounts);
        renderDiscountTable(allDiscounts);

        // Setup filters
        document.getElementById('searchDiscount')?.addEventListener('input', filterDiscounts);
        document.getElementById('filterDiscountStatus')?.addEventListener('change', filterDiscounts);
        document.getElementById('filterDiscountType')?.addEventListener('change', filterDiscounts);

    } catch (err) {
        console.error(err);
        const tbody = document.getElementById('discountTableBody');
        if (tbody) tbody.innerHTML = `<tr><td colspan="6" class="text-center text-red-500">Lỗi tải dữ liệu</td></tr>`;
    }
}

function calculateDiscountStats(discounts) {
    const total = discounts.length;
    const active = discounts.filter(d => d.status === 'active' && (!d.expiryDate || new Date(d.expiryDate) > new Date())).length;
    const expired = discounts.filter(d => d.status === 'expired' || (d.expiryDate && new Date(d.expiryDate) <= new Date())).length;
    const totalUsage = discounts.reduce((sum, d) => sum + (d.usage || 0), 0);

    document.getElementById('discount-total').innerText = total;
    document.getElementById('discount-active').innerText = active;
    document.getElementById('discount-expired').innerText = expired;
    document.getElementById('discount-used').innerText = totalUsage;
}

function renderDiscountTable(discounts) {
    const tbody = document.getElementById('discountTableBody');

    if (discounts.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">Chưa có mã giảm giá nào</td></tr>';
        return;
    }

    tbody.innerHTML = discounts.map(discount => {
        const typeBadge = getDiscountTypeBadge(discount.type);
        const statusBadge = getDiscountStatusBadge(discount);
        const formattedDate = discount.expiryDate ? new Date(discount.expiryDate).toLocaleDateString('vi-VN') : 'Không giới hạn';
        const valueDisplay = discount.type === 'percent'
            ? `${discount.value}%`
            : `${formatCurrency(discount.value)}`;

        return `
            <tr>
                <td style="font-weight: 600; color: #3b82f6;">${discount.code}</td>
                <td>${typeBadge}</td>
                <td style="font-weight: 600;">${valueDisplay}</td>
                <td style="color: #666; font-size: 0.875rem;">${formattedDate}</td>
                <td>${statusBadge}</td>
                <td>
                    <button class="action-btn" title="Kích hoạt/Vô hiệu hóa" onclick="toggleDiscountStatus('${discount._id}')">
                        <i class="fas ${discount.status === 'active' ? 'fa-toggle-on' : 'fa-toggle-off'}" 
                           style="color: ${discount.status === 'active' ? '#10b981' : '#6b7280'};"></i>
                    </button>
                    <button class="action-btn" title="Sửa" onclick="editDiscount('${discount._id}')">
                        <i class="fas fa-edit" style="color: #3b82f6;"></i>
                    </button>
                    <button class="action-btn" title="Xóa" onclick="deleteDiscount('${discount._id}')">
                        <i class="fas fa-trash-alt" style="color: #ef4444;"></i>
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

function filterDiscounts() {
    const searchVal = document.getElementById('searchDiscount')?.value.toLowerCase() || '';
    const statusFilter = document.getElementById('filterDiscountStatus')?.value || '';
    const typeFilter = document.getElementById('filterDiscountType')?.value || '';

    let filtered = allDiscounts.filter(discount => {
        const matchesSearch = !searchVal || discount.code.toLowerCase().includes(searchVal);
        const matchesStatus = !statusFilter || discount.status === statusFilter;
        const matchesType = !typeFilter || discount.type === typeFilter;

        return matchesSearch && matchesStatus && matchesType;
    });

    renderDiscountTable(filtered);
}

window.openAddDiscountModal = function () {
    document.getElementById('discountForm').reset();
    document.getElementById('discountId').value = '';
    document.getElementById('discountModalTitle').innerText = 'Thêm mã giảm giá mới';
    document.getElementById('discountModal').classList.remove('hidden');
}

window.closeDiscountModal = function () {
    document.getElementById('discountModal').classList.add('hidden');
}

window.saveDiscount = async function (e) {
    e.preventDefault();

    const id = document.getElementById('discountId').value;
    const data = {
        code: document.getElementById('discCode').value,
        type: document.getElementById('discType').value,
        value: parseFloat(document.getElementById('discValue').value),
        maxValue: parseFloat(document.getElementById('discMaxValue').value) || 0,
        status: document.getElementById('discStatus').value,
        usageLimit: parseInt(document.getElementById('discUsageLimit').value) || 0,
        expiryDate: document.getElementById('discExpiryDate').value || null
    };

    const method = id ? 'PUT' : 'POST';
    const url = id
        ? `${CONFIG.API_URL}/discounts/${id}`
        : `${CONFIG.API_URL}/discounts`;

    try {
        const res = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            alert('Lưu mã giảm giá thành công!');
            closeDiscountModal();
            const contentDiv = document.getElementById('content');
            renderDiscounts(contentDiv);
        } else {
            alert('Lỗi khi lưu mã giảm giá');
        }
    } catch (err) {
        console.error(err);
        alert('Lỗi kết nối');
    }
}

window.editDiscount = function (id) {
    const discount = allDiscounts.find(d => d._id === id);
    if (!discount) return;

    openAddDiscountModal();
    document.getElementById('discountModalTitle').innerText = 'Cập nhật mã giảm giá';
    document.getElementById('discountId').value = discount._id;
    document.getElementById('discCode').value = discount.code;
    document.getElementById('discType').value = discount.type;
    document.getElementById('discValue').value = discount.value;
    document.getElementById('discMaxValue').value = discount.maxValue || '';
    document.getElementById('discStatus').value = discount.status;
    document.getElementById('discUsageLimit').value = discount.usageLimit || '';

    if (discount.expiryDate) {
        const date = new Date(discount.expiryDate);
        document.getElementById('discExpiryDate').value = date.toISOString().split('T')[0];
    }
}

window.toggleDiscountStatus = async function (id) {
    const discount = allDiscounts.find(d => d._id === id);
    if (!discount) return;

    const newStatus = discount.status === 'active' ? 'expired' : 'active';

    try {
        const res = await fetch(`${CONFIG.API_URL}/discounts/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ ...discount, status: newStatus })
        });

        if (res.ok) {
            const contentDiv = document.getElementById('content');
            renderDiscounts(contentDiv);
        } else {
            alert('Lỗi khi cập nhật trạng thái');
        }
    } catch (err) {
        console.error(err);
    }
}

window.deleteDiscount = async function (id) {
    if (!confirm('Bạn có chắc muốn xóa mã giảm giá này?')) return;

    try {
        const res = await fetch(`${CONFIG.API_URL}/discounts/${id}`, {
            method: 'DELETE'
        });

        if (res.ok) {
            alert('Đã xóa mã giảm giá');
            const contentDiv = document.getElementById('content');
            renderDiscounts(contentDiv);
        } else {
            alert('Lỗi khi xóa');
        }
    } catch (err) {
        console.error(err);
    }
}

function getDiscountTypeBadge(type) {
    const typeMap = {
        'percent': { text: 'Phần trăm', class: 'badge-processing' },
        'fixed': { text: 'Cố định', class: 'badge-delivered' },
        'shipping': { text: 'Miễn phí ship', class: 'badge-shipped' }
    };

    const badge = typeMap[type] || { text: type, class: 'badge-pending' };
    return `<span class="status-badge ${badge.class}">${badge.text}</span>`;
}

function getDiscountStatusBadge(discount) {
    const isExpired = discount.expiryDate && new Date(discount.expiryDate) <= new Date();
    const status = isExpired || discount.status === 'expired' ? 'expired' : 'active';

    const statusMap = {
        'active': { text: 'Còn hiệu lực', class: 'badge-delivered' },
        'expired': { text: 'Hết hạn', class: 'badge-cancelled' }
    };

    const badge = statusMap[status];
    return `<span class="status-badge ${badge.class}">${badge.text}</span>`;
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}
