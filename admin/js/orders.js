// Order Management Functions

let allOrders = [];
let currentOrderId = null;

async function renderOrders(container) {
    const tpl = document.getElementById('tpl-orders');
    container.innerHTML = '';
    container.appendChild(tpl.content.cloneNode(true));

    try {
        const response = await fetch(`${CONFIG.API_URL}/orders`);
        allOrders = await response.json();

        calculateOrderStats(allOrders);
        renderOrderTable(allOrders);

        document.getElementById('searchOrder').addEventListener('input', filterOrders);
        document.getElementById('filterOrderStatus').addEventListener('change', filterOrders);
        document.getElementById('filterOrderPayment').addEventListener('change', filterOrders);

    } catch (err) {
        console.error(err);
        const tbody = document.getElementById('orderTableBody');
        if (tbody) tbody.innerHTML = `<tr><td colspan="6" class="text-center text-red-500">Lỗi tải dữ liệu</td></tr>`;
    }
}

function calculateOrderStats(orders) {
    const total = orders.length;
    const newOrders = orders.filter(o => o.status === 'Pending').length;
    const processing = orders.filter(o => o.status === 'Processing').length;
    const revenue = orders.filter(o => o.status === 'Delivered').reduce((sum, o) => sum + o.totalPrice, 0);

    document.getElementById('order-total').innerText = total;
    document.getElementById('order-new').innerText = newOrders;
    document.getElementById('order-processing').innerText = processing;
    document.getElementById('order-revenue').innerText = formatCurrency(revenue);
}

function renderOrderTable(orders) {
    const tbody = document.getElementById('orderTableBody');

    if (orders.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="text-center">Chưa có đơn hàng nào</td></tr>';
        return;
    }

    tbody.innerHTML = orders.map(order => {
        const customerName = order.user?.name || 'Khách vãng lai';
        const statusBadge = getStatusBadge(order.status);
        const formattedDate = new Date(order.createdAt).toLocaleDateString('vi-VN');
        const orderId = order._id.substring(order._id.length - 6).toUpperCase();

        return `
            <tr>
                <td style="font-weight: 600; color: #3b82f6;">#${orderId}</td>
                <td>${customerName}</td>
                <td style="color: #10b981; font-weight: 600;">${formatCurrency(order.totalPrice)}</td>
                <td>${statusBadge}</td>
                <td>${formattedDate}</td>
                <td>
                    <button class="action-btn" title="Xem chi tiết" onclick="viewOrderDetail('${order._id}')">
                        <i class="fas fa-eye" style="color: #3b82f6;"></i>
                    </button>
                </td>
            </tr>
        `;
    }).join('');
}

function filterOrders() {
    const searchVal = document.getElementById('searchOrder').value.toLowerCase();
    const statusFilter = document.getElementById('filterOrderStatus').value;
    const paymentFilter = document.getElementById('filterOrderPayment').value;

    let filtered = allOrders.filter(order => {
        const matchesSearch = !searchVal ||
            order._id.toLowerCase().includes(searchVal) ||
            order.user?.name?.toLowerCase().includes(searchVal);

        const matchesStatus = !statusFilter || order.status === statusFilter;

        const matchesPayment = !paymentFilter ||
            order.paymentMethod.toLowerCase() === paymentFilter.toLowerCase();

        return matchesSearch && matchesStatus && matchesPayment;
    });

    renderOrderTable(filtered);
}

window.viewOrderDetail = async function (orderId) {
    currentOrderId = orderId;

    try {
        const response = await fetch(`${CONFIG.API_URL}/orders/${orderId}`);
        const order = await response.json();

        const shortId = orderId.substring(orderId.length - 6).toUpperCase();
        document.getElementById('modalOrderId').innerText = `#${shortId}`;

        document.getElementById('modalCustomerName').innerText = order.user?.name || 'Khách vãng lai';
        document.getElementById('modalCustomerPhone').innerText = order.shippingAddress?.phone || '-';
        document.getElementById('modalShippingAddress').innerText =
            `${order.shippingAddress?.address || ''}, ${order.shippingAddress?.city || ''}`;

        document.getElementById('modalOrderDate').innerText = new Date(order.createdAt).toLocaleString('vi-VN');
        document.getElementById('modalPaymentMethod').innerText = order.paymentMethod;
        document.getElementById('modalOrderStatus').value = order.status;

        const itemsBody = document.getElementById('modalOrderItems');
        itemsBody.innerHTML = order.orderItems.map(item => `
            <tr>
                <td>${item.name}</td>
                <td>${item.qty}</td>
                <td>${formatCurrency(item.price)}</td>
                <td class="font-medium">${formatCurrency(item.price * item.qty)}</td>
            </tr>
        `).join('');

        document.getElementById('modalTotalPrice').innerText = formatCurrency(order.totalPrice);

        document.getElementById('orderDetailModal').classList.remove('hidden');

    } catch (err) {
        console.error(err);
        alert('Không thể tải chi tiết đơn hàng');
    }
}

window.closeOrderDetail = function () {
    document.getElementById('orderDetailModal').classList.add('hidden');
    currentOrderId = null;
}

window.saveOrderStatus = async function () {
    if (!currentOrderId) return;

    const newStatus = document.getElementById('modalOrderStatus').value;

    try {
        const response = await fetch(`${CONFIG.API_URL}/orders/${currentOrderId}/status`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ status: newStatus })
        });

        if (response.ok) {
            alert('Cập nhật trạng thái thành công!');
            closeOrderDetail();

            const contentDiv = document.getElementById('content');
            renderOrders(contentDiv);
        } else {
            alert('Lỗi khi cập nhật trạng thái');
        }
    } catch (err) {
        console.error(err);
        alert('Lỗi kết nối');
    }
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN', {
        style: 'currency',
        currency: 'VND'
    }).format(amount);
}

function getStatusBadge(status) {
    const statusMap = {
        'Pending': { text: 'Chờ xác nhận', class: 'badge-pending' },
        'Processing': { text: 'Đang xử lý', class: 'badge-processing' },
        'Shipped': { text: 'Đang giao', class: 'badge-shipped' },
        'Delivered': { text: 'Đã giao', class: 'badge-delivered' },
        'Cancelled': { text: 'Đã hủy', class: 'badge-cancelled' }
    };

    const badge = statusMap[status] || { text: status, class: 'badge-pending' };
    return `<span class="status-badge ${badge.class}">${badge.text}</span>`;
}
