const express = require('express');
const router = express.Router();
const Order = require('../models/Order');

// @route   GET /api/orders
// @desc    Get all orders
<<<<<<< HEAD
// @access  Staff/Admin
router.get('/', async (req, res) => {
    try {
        const orders = await Order.find().populate('userId', 'name email').sort({ createdAt: -1 });
=======
// @access  Public (for now, usually Admin)
router.get('/', async (req, res) => {
    try {
        const orders = await Order.find().sort({ createdAt: -1 });
>>>>>>> upstream/main
        res.json(orders);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

<<<<<<< HEAD
// @route   GET /api/orders/:id
// @desc    Get order by ID
// @access  Staff/Admin
router.get('/:id', async (req, res) => {
    try {
        const order = await Order.findById(req.params.id).populate('userId', 'name email');
        if (!order) {
            return res.status(404).json({ msg: 'Order not found' });
        }
        res.json(order);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// @route   PUT /api/orders/:id/status
// @desc    Update order status
// @access  Staff/Admin
router.put('/:id/status', async (req, res) => {
    try {
        const { status } = req.body;
        const order = await Order.findById(req.params.id);
        
        if (!order) {
            return res.status(404).json({ msg: 'Order not found' });
        }

        order.status = status;
        order.updatedAt = new Date();
        await order.save();

        res.json(order);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// @route   POST /api/orders
// @desc    Create new order
// @access  Public
router.post('/', async (req, res) => {
    try {
        const { userId, items, totalAmount, shippingAddress, phone, customerName } = req.body;

        const order = new Order({
            userId,
            items,
            totalAmount,
            shippingAddress,
            phone,
            customerName,
            status: 'pending'
        });

        await order.save();
        res.json(order);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

=======
>>>>>>> upstream/main
module.exports = router;
