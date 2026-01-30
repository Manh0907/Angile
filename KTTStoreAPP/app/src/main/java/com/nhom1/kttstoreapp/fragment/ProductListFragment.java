package com.nhom1.kttstoreapp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
=======
import android.widget.Button;
import android.widget.LinearLayout;
>>>>>>> upstream/main
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.kttstoreapp.R;
import com.nhom1.kttstoreapp.adapter.ProductAdapter;
import com.nhom1.kttstoreapp.api.ApiClient;
import com.nhom1.kttstoreapp.api.ApiService;
import com.nhom1.kttstoreapp.model.Category;
import com.nhom1.kttstoreapp.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListFragment extends Fragment {

    private RecyclerView rvAllProducts;
    private ProductAdapter productAdapter;
<<<<<<< HEAD
    private EditText etSearch;
    private ImageView ivFilter;
    private LinearLayout llFilters;
    private Spinner spinnerCategory;
    private Spinner spinnerPriceRange;

    private List<Product> allProducts = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private String selectedCategoryId = "";
    private String selectedPriceRange = "";
=======
    private LinearLayout llCategories;
    private List<Product> productList = new ArrayList<>();

    private ApiService apiService;

    private int currentPage = 1;
    private int limit = 10;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String currentCategoryId = null;

    // Advanced Filter State
    private Integer currentMinPrice, currentMaxPrice;
    private String currentSort, currentStock;
    private Boolean currentPromotion;
>>>>>>> upstream/main

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);

        rvAllProducts = view.findViewById(R.id.rvAllProducts);
<<<<<<< HEAD
        etSearch = view.findViewById(R.id.etSearch);
        ivFilter = view.findViewById(R.id.ivFilter);
        llFilters = view.findViewById(R.id.llFilters);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        spinnerPriceRange = view.findViewById(R.id.spinnerPriceRange);

        rvAllProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        setupSearch();
        setupFilters();
        loadCategories();
        loadProducts();
=======
        llCategories = view.findViewById(R.id.llCategories);

        view.findViewById(R.id.ivBack).setOnClickListener(v -> {
            if (getActivity() != null)
                getActivity().onBackPressed();
        });

        view.findViewById(R.id.ivFilter).setOnClickListener(v -> showFilterBottomSheet());

        apiService = ApiClient.getClient().create(ApiService.class);

        setupRecyclerView();
        loadCategories();
        loadProducts(true);
>>>>>>> upstream/main

        return view;
    }

<<<<<<< HEAD
    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFilters() {
        ivFilter.setOnClickListener(v -> {
            if (llFilters.getVisibility() == View.VISIBLE) {
                llFilters.setVisibility(View.GONE);
            } else {
                llFilters.setVisibility(View.VISIBLE);
            }
        });

        // Setup price range spinner
        String[] priceRanges = {"Tất cả giá", "Dưới 100.000đ", "100.000đ - 500.000đ", "500.000đ - 1.000.000đ", "Trên 1.000.000đ"};
        ArrayAdapter<String> priceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, priceRanges);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriceRange.setAdapter(priceAdapter);

        spinnerPriceRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPriceRange = priceRanges[position];
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedCategoryId = "";
                } else {
                    selectedCategoryId = categories.get(position - 1).getId();
                }
                filterProducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadCategories() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories = response.body();
                    setupCategorySpinner();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // Silent fail - categories are optional
            }
        });
    }

    private void setupCategorySpinner() {
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("Tất cả danh mục");
        for (Category category : categories) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
    }

    private void loadProducts() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getProducts().enqueue(new Callback<List<Product>>() {
=======
    private void showFilterBottomSheet() {
        FilterBottomSheetFragment filterFragment = new FilterBottomSheetFragment();
        filterFragment.setOnFilterApplyListener((minPrice, maxPrice, sort, stock, promotion) -> {
            this.currentMinPrice = minPrice;
            this.currentMaxPrice = maxPrice;
            this.currentSort = sort;
            this.currentStock = stock;
            this.currentPromotion = promotion;

            // Reset pagination
            currentPage = 1;
            isLastPage = false;
            loadProducts(true);
        });
        filterFragment.show(getParentFragmentManager(), filterFragment.getTag());
    }

    private void setupRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvAllProducts.setLayoutManager(layoutManager);

        productAdapter = new ProductAdapter(getContext(), productList, product -> {
            // Handle product click
            Toast.makeText(getContext(), "Selected: " + product.getName(), Toast.LENGTH_SHORT).show();
        });
        rvAllProducts.setAdapter(productAdapter);

        rvAllProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
>>>>>>> upstream/main
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        loadProducts(false);
                    }
                }
            }
        });
    }

    private void loadCategories() {
        apiService.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
<<<<<<< HEAD
                    allProducts = response.body();
                    filterProducts();
=======
                    setupCategoryFilters(response.body());
>>>>>>> upstream/main
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                // Ignore error for categories
            }
        });
    }

<<<<<<< HEAD
    private void filterProducts() {
        List<Product> filteredProducts = new ArrayList<>();

        String searchQuery = etSearch.getText().toString().toLowerCase().trim();

        for (Product product : allProducts) {
            // Filter by search query
            if (!searchQuery.isEmpty()) {
                if (!product.getName().toLowerCase().contains(searchQuery)) {
                    continue;
                }
            }

            // Filter by category
            if (!selectedCategoryId.isEmpty()) {
                if (product.getCategoryId() == null || !product.getCategoryId().equals(selectedCategoryId)) {
                    continue;
                }
            }

            // Filter by price range
            if (!selectedPriceRange.isEmpty() && !selectedPriceRange.equals("Tất cả giá")) {
                double price = product.getPrice();
                switch (selectedPriceRange) {
                    case "Dưới 100.000đ":
                        if (price >= 100000) continue;
                        break;
                    case "100.000đ - 500.000đ":
                        if (price < 100000 || price > 500000) continue;
                        break;
                    case "500.000đ - 1.000.000đ":
                        if (price < 500000 || price > 1000000) continue;
                        break;
                    case "Trên 1.000.000đ":
                        if (price <= 1000000) continue;
                        break;
                }
            }

            filteredProducts.add(product);
        }

        productAdapter = new ProductAdapter(getContext(), filteredProducts);
        rvAllProducts.setAdapter(productAdapter);
=======
    private void setupCategoryFilters(List<Category> categories) {
        // Add "All" button
        addCategoryButton("Tất cả", null);

        for (Category category : categories) {
            addCategoryButton(category.getName(), category.getId());
        }
    }

    private void addCategoryButton(String name, String categoryId) {
        Button btn = new Button(getContext());
        btn.setText(name);
        btn.setAllCaps(false);
        btn.setTextSize(12);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(8, 0, 8, 0);
        btn.setLayoutParams(params);

        if ((currentCategoryId == null && categoryId == null) ||
                (currentCategoryId != null && currentCategoryId.equals(categoryId))) {
            btn.setBackgroundColor(Color.BLACK);
            btn.setTextColor(Color.WHITE);
        } else {
            btn.setBackgroundColor(Color.LTGRAY);
            btn.setTextColor(Color.BLACK);
        }

        btn.setOnClickListener(v -> {
            currentCategoryId = categoryId;
            currentPage = 1;
            isLastPage = false;
            reloadCategoryButtons();
            loadProducts(true);
        });

        llCategories.addView(btn);
    }

    private void reloadCategoryButtons() {
        // Simple way: Clear and reload (in real app, just update state)
        // For now, we will just iterate and update simple background colors if we
        // stored reference
        // Ideally re-render logic. Since we fetch categories once, we should store
        // them.
        // For simplicity in this generated code, we will assume user won't spam filters
        // too fast
        // or we simply re-request categories or keep them in a list.
        // Let's just visually update the existing buttons if possible,
        // but since we didn't keep references, let's just clear and re-fetch or
        // simpler:
        // just reset the whole view.
        // BETTER APPROACH: Just re-call loadCategories or optimize later.
        // For this task, let's just clear views and re-call loadCategories logic if we
        // had the list.
        // Since we didn't store list, let's correct this.

        llCategories.removeAllViews();
        loadCategories(); // This causes network call again, which is suboptimal but works for MVP.
    }

    private void loadProducts(boolean isFirstPage) {
        isLoading = true;
        if (isFirstPage) {
            currentPage = 1;
        }

        apiService.getProducts(null, currentPage, limit, currentCategoryId,
                currentMinPrice, currentMaxPrice, currentSort, currentStock, currentPromotion)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null) {
                            List<Product> newProducts = response.body();

                            if (isFirstPage) {
                                productList.clear();
                                productAdapter.clearProducts(); // Helper if exists, or just notifyDataSetChanged
                            }

                            if (newProducts.isEmpty()) {
                                isLastPage = true;
                            } else {
                                productList.addAll(newProducts);
                                productAdapter.notifyDataSetChanged();
                                if (newProducts.size() < limit) {
                                    isLastPage = true;
                                } else {
                                    currentPage++;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        isLoading = false;
                        Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
>>>>>>> upstream/main
    }
}
