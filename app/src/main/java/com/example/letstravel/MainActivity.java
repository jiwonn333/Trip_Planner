package com.example.letstravel;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.letstravel.fragment.information.InformationViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleMap googleMap;
    private CameraPosition cameraPosition;
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationProviderClient;

    /**
     * 위치 권한이 부여되지 않은 경우 사용할 기본 위치(서울) 및 기본 확대/축소
     */
    private final LatLng defaultLocation = new LatLng(37.56, 126.97);
    private static final int DEFAULT_ZOOM = 20;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;


    /**
     * 장치가 현재 위치한 지리적 위치. 즉, Fused Location Provider에서 검색한 마지막으로 알려진 위치
     */
    private Location lastKnownLocation;

    /**
     * 활동 상태를 저장하기 위한 키.
     */
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    /**
     * 현재 위치를 선택하는 데 사용
     */
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * {@link#onRequestPermissionsResult(int, String[], int[])}에서 반환된 후
     * 요청된 권한이 거부되었는지 여부를 나타내는 플래그
     */
    private boolean permissionDenied = false;


    private Marker currentMarker = null;
    private InformationViewModel informationViewModel;

    private String title;
    private double lat;
    private double lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 저장된 인스턴스 상태에서 위치 및 카메라 위치를 검색
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        init();
        initAutoComplete();
    }

    private void init() {
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        placesClient = Places.createClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
        mapFragment.getMapAsync(this);

        // bottom navigation 탐색 설정
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_container);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNav, navController);
    }

    /**
     * 활동이 일시 중지되었을 때 지도의 상태를 저장
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        return true;
    }

    /**
     * 메뉴 옵션의 클릭을 처리하여 장소를 가져오기
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();
        }
        return true;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        // 사용자 정의 정보 창 어댑터를 사용하여 정보 창 콘텐츠에서 여러 줄의 텍스트를 처리
        this.googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Nullable
            @Override
            // 정보 창, 제목 및 스니펫의 레이아웃을 확장
            public View getInfoContents(@NonNull Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map_container), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }

            @Nullable
            @Override
            // getInfoContents()가 다음에 호출되도록 여기에서 null을 반환
            public View getInfoWindow(@NonNull Marker marker) {
                return null;
            }
        });
        // 사용자에게 권한을 요청
        getLocationPermission();

        // 지도에서 내 위치 레이어 및 관련 컨트롤을 켬
        updateLocationUI();

        // 장치의 현재 위치를 가져오고 지도의 위치를 설정
        getDeviceLocation();
    }

    /**
     * 위치 권한 띄우기
     */
    private void getLocationPermission() {
        /**
         * 기기의 위치를 알 수 있도록 위치 권한을 요청
         * 권한 요청의 결과는 onRequestPermissionsResult 콜백에 의해 처리
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    // 위치 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    private void getDeviceLocation() {
        /**
         * 위치를 사용할 수 없는 드문 경우에 null일 수 있는 장치의 가장 최근의 최상의 위치를 가져오기
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // 지도의 카메라 위치를 기기의 현재 위치로 설정
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "현재 위치는 null입니다.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            googleMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * 사용자가 위치 권한을 부여했는지 여부에 따라 지도의 UI 설정을 업데이트 한다.
     */
    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void showCurrentPlace() {
        if (googleMap == null) {
            return;
        }

        if (locationPermissionGranted) {
            //필드를 사용하여 반환할 데이터 유형을 정의
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // 빌더를 사용하여 FindCurrentPlaceRequest를 작성
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // 가능성이 높은 장소, 즉 기기의 현재 위치와 가장 잘 일치하는 업체 및 기타 관심 장소를 가져옴
            @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    FindCurrentPlaceResponse likelyPlaces = task.getResult();

                    // 개수를 설정하고 반환되는 항목이 5개 미만인 경우를 처리
                    int count;
                    if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                        count = likelyPlaces.getPlaceLikelihoods().size();
                    } else {
                        count = M_MAX_ENTRIES;
                    }

                    int i = 0;
                    likelyPlaceNames = new String[count];
                    likelyPlaceAddresses = new String[count];
                    likelyPlaceAttributions = new List[count];
                    likelyPlaceLatLngs = new LatLng[count];

                    for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                        // 사용자에게 보여줄 가능성이 있는 장소 목록 작성
                        likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                        likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                        likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                .getAttributions();
                        likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                        i++;
                        if (i > (count - 1)) {
                            break;
                        }
                    }

                    // 사용자에게 가능한 장소 목록을 제공하는 대화 상자를 표시하고 선택한 장소에 마커를 추가
                    MainActivity.this.openPlacesDialog();
                } else {
                    Log.e(TAG, "Exception: %s", task.getException());
                }
            });
        } else {
            Log.i(TAG, "사용자가 권한을 부여하지 않았습니다.");

            // 사용자가 장소를 선택하지 않았으므로 기본 마커를 추가
            googleMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.title_information))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // 사용자에게 권한을 요청
            getLocationPermission();
        }
    }

    /**
     * 사용자가 가능한 장소 목록에서 장소를 선택할 수 있는 양식을 표시
     */
    private void openPlacesDialog() {
        // 사용자에게 현재 있는 장소를 선택하도록 요청합니다.
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            // which - 선택한 항목의 위치를 포함
            LatLng markerLatLng = likelyPlaceLatLngs[which];
            String markerSnippet = likelyPlaceAddresses[which];
            if (likelyPlaceAttributions[which] != null) {
                markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
            }

            // 해당 장소에 대한 정보를 표시하는 정보 창과 함께 선택한 장소에 대한 마커를 추가
            googleMap.addMarker(new MarkerOptions()
                    .title(likelyPlaceNames[which])
                    .position(markerLatLng)
                    .snippet(markerSnippet));

            // 마커 위치에 지도의 카메라를 배치
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                    DEFAULT_ZOOM));
        };

        // dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.title_mypage)
                .setItems(likelyPlaceNames, listener)
                .show();
    }


    private void initAutoComplete() {
        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteSupportFragment.setTypesFilter(Arrays.asList("restaurant", "subway_station", "store", "cafe", "bar"));
        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)));
        autocompleteSupportFragment.setCountries("KR");
        autocompleteSupportFragment.setHint(getString(R.string.search_text));
        autocompleteSupportFragment.setActivityMode(AutocompleteActivityMode.FULLSCREEN);
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                informationViewModel = new ViewModelProvider(MainActivity.this).get(InformationViewModel.class);
                informationViewModel.setTitle(place.getName());
                informationViewModel.setLatitude(Objects.requireNonNull(place.getLatLng()).latitude);
                informationViewModel.setLongitude(place.getLatLng().longitude);

                informationViewModel.getTitle().observe(MainActivity.this, s -> title = s);

                informationViewModel.getLatitude().observe(MainActivity.this, aDouble -> lat = aDouble);

                informationViewModel.getLongitude().observe(MainActivity.this, aDouble -> lng = aDouble);


                // 해당 장소에 대한 정보를 표시하는 정보 창과 함께 선택한 장소에 대한 마커를 추가
                googleMap.addMarker(new MarkerOptions()
                        .title(title)
                        .position(new LatLng(lat, lng))
                        .snippet(place.getAddress()));

                // 마커 위치에 지도의 카메라를 배치
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), DEFAULT_ZOOM));


            }

            @Override
            public void onError(@NonNull Status status) {
                Log.e("test", "error : " + status);
            }
        });
    }
}