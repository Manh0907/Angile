const express = require('express');
const router = express.Router();
const Promotion = require('../models/Promotion');

// GET /api/promotions
router.get('/', async (req, res) => {
    try {
        const list = await Promotion.find().sort({ createdAt: -1 });
        res.json(list);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// POST /api/promotions
router.post('/', async (req, res) => {
    try {
        const data = req.body;
        const promo = new Promotion({
            title: data.title,
            description: data.description,
            discountText: data.discountText,
            type: data.type || 'flash_sale',
            status: data.status || 'inactive',
            productCount: data.productCount || 0,
            categoryCount: data.categoryCount || 0,
            startDate: data.startDate || null,
            endDate: data.endDate || null
        });
        await promo.save();
        res.status(201).json(promo);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// PUT /api/promotions/:id
router.put('/:id', async (req, res) => {
    try {
        const data = req.body;
        const update = {
            title: data.title,
            description: data.description,
            discountText: data.discountText,
            type: data.type,
            status: data.status,
            productCount: data.productCount,
            categoryCount: data.categoryCount,
            startDate: data.startDate,
            endDate: data.endDate
        };

        const promo = await Promotion.findByIdAndUpdate(
            req.params.id,
            update,
            { new: true }
        );

        if (!promo) {
            return res.status(404).json({ msg: 'Promotion not found' });
        }
        res.json(promo);
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

// DELETE /api/promotions/:id
router.delete('/:id', async (req, res) => {
    try {
        const promo = await Promotion.findByIdAndDelete(req.params.id);
        if (!promo) {
            return res.status(404).json({ msg: 'Promotion not found' });
        }
        res.json({ msg: 'Promotion removed' });
    } catch (err) {
        console.error(err.message);
        res.status(500).send('Server Error');
    }
});

module.exports = router;

