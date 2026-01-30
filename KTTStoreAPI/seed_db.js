const mongoose = require('mongoose');
const Product = require('./models/Product');
const Category = require('./models/Category');
const User = require('./models/User');
const bcrypt = require('bcryptjs');

const seedData = async () => {
    try {
        await mongoose.connect('mongodb://localhost:27017/kttstore');
        console.log('Connected to DB');

        await Category.deleteMany({});
        await Product.deleteMany({});
        await User.deleteMany({});
        console.log('Cleared old data');

        // Create Categories
        const categories = [
            { name: 'Áo', image: 'https://via.placeholder.com/100?text=Áo' },
            { name: 'Quần', image: 'https://via.placeholder.com/100?text=Quần' },
            { name: 'Giày dép', image: 'https://via.placeholder.com/100?text=Giày' },
            { name: 'Phụ kiện', image: 'https://via.placeholder.com/100?text=PK' },
            { name: 'Túi xách', image: 'https://via.placeholder.com/100?text=Túi' }
        ];

        const createdCategories = await Category.insertMany(categories);
        console.log('Categories created:', createdCategories.length);

        // Create Sample Products
        const products = [
            {
                name: 'Áo thun nam basic',
                price: 150000,
                oldPrice: 200000,
                categoryId: createdCategories[0]._id,
                gender: 'Nam',
                countInStock: 50,
                image: 'https://bizweb.dktcdn.net/100/287/440/products/2-626ce9e8-29a2-4398-9004-19f8d6f8fb79.png',
                status: 'active'
            },
            {
                name: 'Áo sơ mi nữ trắng',
                price: 250000,
                oldPrice: 350000,
                categoryId: createdCategories[0]._id,
                gender: 'Nữ',
                countInStock: 30,
                image: 'https://bizweb.dktcdn.net/100/287/440/products/ao-so-mi-nu-trang.jpg',
                status: 'active'
            },
            {
                name: 'Quần jean nam slimfit',
                price: 450000,
                oldPrice: 600000,
                categoryId: createdCategories[1]._id,
                gender: 'Nam',
                countInStock: 40,
                image: 'https://bizweb.dktcdn.net/100/287/440/products/quan-jean-nam.jpg',
                status: 'active'
            },
            {
                name: 'Váy midi nữ',
                price: 380000,
                categoryId: createdCategories[1]._id,
                gender: 'Nữ',
                countInStock: 25,
                image: 'https://bizweb.dktcdn.net/100/287/440/products/vay-midi-nu.jpg',
                status: 'active'
            },
            {
                name: 'Giày sneaker nam',
                price: 650000,
                oldPrice: 800000,
                categoryId: createdCategories[2]._id,
                gender: 'Nam',
                countInStock: 20,
                image: 'https://bizweb.dktcdn.net/100/287/440/products/giay-sneaker-nam.jpg',
                status: 'active'
            },
            {
                name: 'Giày cao gót nữ',
                price: 550000,
                categoryId: createdCategories[2]._id,
                gender: 'Nữ',
                countInStock: 15,
                image: 'https://bizweb.dktcdn.net/100/287/440/products/giay-cao-got-nu.jpg',
                status: 'active'
            },
            {
                name: 'Túi xách nữ cao cấp',
                price: 890000,
                oldPrice: 1200000,
                categoryId: createdCategories[4]._id,
                gender: 'Nữ',
                countInStock: 10,
                image: 'https://bizweb.dktcdn.net/100/287/440/products/tui-xach-nu.jpg',
                status: 'active'
            },
            {
                name: 'Ví nam da thật',
                price: 320000,
                categoryId: createdCategories[3]._id,
                gender: 'Nam',
                countInStock: 35,
                image: 'https://bizweb.dktcdn.net/100/287/440/products/vi-nam-da.jpg',
                status: 'active'
            },
            {
                name: 'Thắt lưng nam',
                price: 180000,
                oldPrice: 250000,
                categoryId: createdCategories[3]._id,
                gender: 'Nam',
                countInStock: 45,
                image: 'https://bizweb.dktcdn.net/100/287/440/products/that-lung-nam.jpg',
                status: 'active'
            },
            {
                name: 'Khăn choàng nữ',
                price: 120000,
                categoryId: createdCategories[3]._id,
                gender: 'Nữ',
                countInStock: 60,
                image: 'https://bizweb.dktcdn.net/100/287/440/products/khan-choang-nu.jpg',
                status: 'active'
            }
        ];

        await Product.insertMany(products);
        console.log('Products created:', products.length);

        // Create Users
        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash('123456', salt);

        const adminUser = new User({
            name: 'Admin User',
            email: 'admin@kttstore.com',
            password: hashedPassword,
            phone: '0987654321',
            gender: 'Nam',
            role: 'admin',
            status: 'active'
        });

        await adminUser.save();


    } catch (err) {
        console.error('Error:', err);
    } finally {
        await mongoose.disconnect();
    }
};

seedData();
