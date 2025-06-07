package com.example.onlinecoursesapp.fragments.home;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.adapter.PopularSearchAdapter;
import com.example.onlinecoursesapp.adapter.SearchResultAdapter;
import com.example.onlinecoursesapp.api.ApiClient;
import com.example.onlinecoursesapp.api.CourseApiService;
import com.example.onlinecoursesapp.models.Course;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";

    private EditText etSearch;
    private RecyclerView rvPopularSearches;
    private RecyclerView rvSearchResults;
    private TextView tvPopularSearches;
    private TextView tvNoResults;
    private ProgressBar progressBar;
    private PopularSearchAdapter popularSearchAdapter;
    private SearchResultAdapter searchResultAdapter;

    private CourseApiService apiService;
    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;

    private static final long SEARCH_DELAY = 500; // 500ms delay

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupApi();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initViews(view);
        setupPopularSearches();
        setupSearchResults();
        setupSearchListener();

        return view;
    }

    private void initViews(View view) {
        etSearch = view.findViewById(R.id.etSearch);
        rvPopularSearches = view.findViewById(R.id.rvPopularSearches);
        rvSearchResults = view.findViewById(R.id.rvSearchResults);
        tvPopularSearches = view.findViewById(R.id.tvPopularSearches);
        tvNoResults = view.findViewById(R.id.tvNoResults);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupApi() {
        apiService = ApiClient.getCourseApiService();
    }

    private void setupPopularSearches() {
        // Popular search terms
        List<String> popularTerms = Arrays.asList(
                "machine learning",
                "project management",
                "data analytics",
                "cybersecurity",
                "digital marketing",
                "google data analytics professional certificate",
                "ai for everyone",
                "google",
                "machine learning specialization",
                "ai in healthcare",
                "data analyst"
        );

        popularSearchAdapter = new PopularSearchAdapter(popularTerms);
        popularSearchAdapter.setOnItemClickListener(searchTerm -> {
            etSearch.setText(searchTerm);
            performSearch(searchTerm);
        });

        rvPopularSearches.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPopularSearches.setAdapter(popularSearchAdapter);
    }

    private void setupSearchResults() {
        searchResultAdapter = new SearchResultAdapter(getContext());
        searchResultAdapter.setOnItemClickListener(new SearchResultAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Course course) {
                // Tạo fragment và truyền dữ liệu
                CourseOverviewFragment fragment = new CourseOverviewFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("courseId", course.getId());
                fragment.setArguments(bundle);

                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment) // ID của FrameLayout
                        .addToBackStack(null)
                        .commit();
            }
        });

        rvSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSearchResults.setAdapter(searchResultAdapter);
    }


    private void setupSearchListener() {
        // Text change listener with debounce
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancel previous search
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                String query = s.toString().trim();
                if (query.isEmpty()) {
                    showPopularSearches();
                } else {
                    // Schedule new search with delay
                    searchRunnable = () -> performSearch(query);
                    searchHandler.postDelayed(searchRunnable, SEARCH_DELAY);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Search action on keyboard
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = etSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                }
                return true;
            }
            return false;
        });
    }

    private void showPopularSearches() {
        rvPopularSearches.setVisibility(View.VISIBLE);
        tvPopularSearches.setVisibility(View.VISIBLE);
        rvSearchResults.setVisibility(View.GONE);
        tvNoResults.setVisibility(View.GONE);
    }

    private void showSearchResults() {
        rvPopularSearches.setVisibility(View.GONE);
        tvPopularSearches.setVisibility(View.GONE);
        rvSearchResults.setVisibility(View.VISIBLE);
        tvNoResults.setVisibility(View.GONE);
    }

    private void showNoResults() {
        rvPopularSearches.setVisibility(View.GONE);
        tvPopularSearches.setVisibility(View.GONE);
        rvSearchResults.setVisibility(View.GONE);
        tvNoResults.setVisibility(View.VISIBLE);
    }

    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void performSearch(String query) {
        showLoading(true);

        Call<List<Course>> call = apiService.searchCourses(query);
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<Course> courses = response.body();

                    if (courses.isEmpty()) {
                        showNoResults();
                    } else {
                        showSearchResults();
                        searchResultAdapter.setCourses(courses);
                    }
                } else {
                    Log.e(TAG, "Search failed with code: " + response.code());
                    showNoResults();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.e(TAG, "Search failed: " + t.getMessage());
                showLoading(false);
                showNoResults();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up handler callbacks to prevent memory leaks
        if (searchHandler != null && searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Show popular searches when fragment resumes
        if (etSearch != null && etSearch.getText().toString().trim().isEmpty()) {
            showPopularSearches();
        }
    }

    // Public method to trigger search from parent activity/fragment
    public void setSearchQuery(String query) {
        if (etSearch != null) {
            etSearch.setText(query);
            performSearch(query);
        }
    }

    // Public method to clear search
    public void clearSearch() {
        if (etSearch != null) {
            etSearch.setText("");
            showPopularSearches();
        }
    }
}