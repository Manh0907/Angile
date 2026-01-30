const express = require('express');
const router = express.Router();
const Discount = require('../models/Discount');

// GET /api/discounts
router.get('/', async (req, res) => {
    try {
        const discounts = await Discount.find().sort({ createdAt: -1 });
        res.json(discounts);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// POST /api/discounts
router.post('/', async (req, res) => {
    try {
        const data = req.body;
        const discount = new Discount({
            code: data.code,
            type: data.type || 'percent',
            value: data.value,
            maxValue: data.maxValue || data.max || 0,
            status: data.status || 'active',
            usage: data.usage || 0,
            usageLimit: data.usageLimit || data.limit || 0,
            expiryDate: data.expiryDate || data.expiry || null
        });
        await discount.save();
        res.status(201).json(discount);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// PUT /api/discounts/:id
router.put('/:id', async (req, res) => {
    try {
        const data = req.body;
        const update = {
            code: data.code,
            type: data.type,
            value: data.value,
            maxValue: data.maxValue,
            status: data.status,
            usage: data.usage,
            usageLimit: data.usageLimit,
            expiryDate: data.expiryDate
        };

        const discount = await Discount.findByIdAndUpdate(
            req.params.id,
            update,
            { new: true }
        );

        if (!discount) {
            return res.status(404).json({ msg: 'Discount not found' });
        }
        res.json(discount);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// DELETE /api/discounts/:id
router.delete('/:id', async (req, res) => {
    try {
        const discount = await Discount.findByIdAndDelete(req.params.id);
        if (!discount) {
            return res.status(404).json({ msg: 'Discount not found' });
        }
        res.json({ msg: 'Discount removed' });
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

module.exports = router;

