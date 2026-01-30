// Category Management Functions

let allCategories = [];

async function renderCategories(container) {
    const tpl = document.getElementById('tpl-categories');
    container.innerHTML = '';
    container.appendChild(tpl.content.cloneNode(true));

    const tbody = document.getElementById('categoryTableBody');
    tbody.innerHTML = '<tr><td colspan="4" class="text-center">Đang tải...</td></tr>';

    try {
        const response = await fetch(`${CONFIG.API_URL}/products/categories`);
        allCategories = await response.json();

        document.getElementById('cat-total').innerText = allCategories.length;
        renderCategoryTable(allCategories);

        document.getElementById('searchCategory').addEventListener('input', (e) => {
            const searchVal = e.target.value.toLowerCase();
            const filtered = allCategories.filter(c => c.name.toLowerCase().includes(searchVal));
            renderCategoryTable(filtered);
        });

        const catForm = document.getElementById('categoryForm');
        if (catForm) {
            const newForm = catForm.cloneNode(true);
            catForm.parentNode.replaceChild(newForm, catForm);
            newForm.addEventListener('submit', window.saveCategory);
        }

    } catch (e) {
        console.error(e);
        tbody.innerHTML = `<tr><td colspan="4" class="text-center text-red-500">Lỗi kết nối: ${e.message}</td></tr>`;
    }
}

function renderCategoryTable(categories) {
    const tbody = document.getElementById('categoryTableBody');

    if (categories.length === 0) {
        tbody.innerHTML = '<tr><td colspan="4" class="text-center">Chưa có danh mục nào</td></tr>';
        return;
    }

    tbody.innerHTML = categories.map(c => `
        <tr>
            <td style="font-weight: 500;">${c.name}</td>
            <td>
                <img src="${c.image}" style="width: 60px; height: 60px; border-radius: 8px; object-fit: cover;" 
                     onerror="this.src='https://via.placeholder.com/60?text=${encodeURIComponent(c.name)}'">
            </td>
            <td style="color: #666;">${new Date(c.createdAt || Date.now()).toLocaleDateString()}</td>
            <td>
                <button class="action-btn" title="Sửa" onclick="editCategory('${c._id}')">
                    <i class="fas fa-edit" style="color: #3b82f6;"></i>
                </button>
                <button class="action-btn" title="Xóa" onclick="deleteCategory('${c._id}')">
                    <i class="fas fa-trash-alt" style="color: #ef4444;"></i>
                </button>
            </td>
        </tr>
    `).join('');
}

window.openAddCategoryModal = function () {
    document.getElementById('categoryForm').reset();
    document.getElementById('categoryId').value = '';
    document.getElementById('categoryModalTitle').innerText = 'Thêm danh mục mới';
    document.getElementById('categoryModal').classList.remove('hidden');
}

window.closeCategoryModal = function () {
    document.getElementById('categoryModal').classList.add('hidden');
}

window.saveCategory = async function (e) {
    e.preventDefault();

    const id = document.getElementById('categoryId').value;
    const data = {
        name: document.getElementById('catName').value,
        image: document.getElementById('catImage').value
    };

    const method = id ? 'PUT' : 'POST';
    const url = id
        ? `${CONFIG.API_URL}/products/categories/${id}`
        : `${CONFIG.API_URL}/products/categories`;

    try {
        const res = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            alert('Lưu danh mục thành công!');
            closeCategoryModal();
            const contentDiv = document.getElementById('content');
            renderCategories(contentDiv);
        } else {
            alert('Lỗi khi lưu danh mục');
        }
    } catch (err) {
        console.error(err);
        alert('Lỗi kết nối');
    }
}

window.editCategory = function (id) {
    const c = allCategories.find(x => x._id === id);
    if (!c) return;

    openAddCategoryModal();
    document.getElementById('categoryModalTitle').innerText = 'Cập nhật danh mục';
    document.getElementById('categoryId').value = c._id;
    document.getElementById('catName').value = c.name;
    document.getElementById('catImage').value = c.image;
}

window.deleteCategory = async function (id) {
    if (!confirm('Bạn có chắc muốn xóa danh mục này?')) return;

    try {
        const res = await fetch(`${CONFIG.API_URL}/products/categories/${id}`, {
            method: 'DELETE'
        });

        if (res.ok) {
            alert('Đã xóa danh mục');
            const contentDiv = document.getElementById('content');
            renderCategories(contentDiv);
        } else {
            alert('Lỗi khi xóa');
        }
    } catch (err) {
        console.error(err);
    }
}
