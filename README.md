# Android-DabeeomapsAPI

본 저장소는 IMSDK를 보다 쉽게 적용하기 위한 튜토리얼 프로젝트를 제공합니다.

## API 문서
- [API 문서로 이동](https://docs.google.com/document/d/1xoOEj1Cjr3eBWwXsoTtHBMRSzQoz5-dYL15Je1l7VMY/edit?usp=sharing)

## 프로젝트 설정

### IMSDK 추가
- ``` imsdk.aar ``` 파일을 프로젝트 내 ``` app/libs/ ``` 안에 넣어줍니다.

### MAPDATA FILE 추가
- ``` mapdata.json ``` 파일을 프로젝트 내 ``` app/src/main/assets/ ``` 안에 넣어줍니다.

### AndroidManifest
- 필수 권한을 추가합니다

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    tools:ignore="ScopedStorage" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

### build.gradle (project)
- 라이브러리를 탐색하기 위하여 아래의 항목을 추가합니다.

```gradle
allprojects {
  repositories {
      google()
      jcenter()

      flatDir {
          dirs 'libs'
      }
  }
}
```


### build.gradle (app)

- Android SDK 의 최소버전을 설정합니다.

```gradle
  minSdkVersion 24
```
- IMSDK 에서 사용된 필수 Dependency 항목을 추가합니다.

```gradle
dependencies {
  // Kotlin
  implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"

  // Gson
  implementation 'com.google.code.gson:gson:2.8.5'
  
  // RxJava2
  implementation 'io.reactivex.rxjava2:rxandroid:2.1.1'
  implementation 'io.reactivex.rxjava2:rxjava:2.2.11'
  implementation 'io.reactivex.rxjava2:rxkotlin:2.4.0'

  // IMSDK
  implementation 'com.dabeeo.imsdk:imsdk-v1.00.00-release:1.00.00@aar'
}
```
- 자바 8 이상을 사용하도록 설정합니다.

```gradle
compileOptions {
  sourceCompatibility JavaVersion.VERSION_1_8
  targetCompatibility JavaVersion.VERSION_1_8
}
```

## 샘플 코드 (Kotlin)
<details>
<summary>지도 불러오기</summary>

### 앱 내에 지도뷰 설정 및 지도데이터가 담긴 JSON 파일로 지도를 불러오는 방법을 설명합니다.

> Layout에 MapView 를 추가합니다.

```xml
<com.dabeeo.imsdk.map.MapView
    android:id="@+id/mapView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:frameRate="60.0"
    app:renderMode="RENDER_WHEN_DIRTY" />
```
> Callback 을 생성합니다.

```kotlin
private val mapCallback: MapCallback = object : MapCallback {
        override fun onSuccess(floors: List<FloorInfo>) {
            floorListAdapter?.setItems(floors)
        }

        override fun onError(e: Exception) {
            e.printStackTrace()
        }

        override fun changeFloor(floorOrder: Int) {
            floorListAdapter?.setFloor(floorOrder)
        }

        override fun onClick(x: Double, y: Double, poi: Poi?) {
        }

        override fun onLongClick(x: Double, y: Double, poi: Poi?) {
        }
    }
```

> MapId 로 지도를 로드합니다.

```kotlin
mapView.startMapByMapId("mapId", mapCallback)
```
</details>

<details>
<summary>지도 제어</summary>

### 지도 내 기능을 제어하는 방법을 설명합니다.

> 지도 Zoom

```kotlin
// 현재 Zoom 레벨을 설정합니다.
// ZoomLevel은 Float형이며 소숫점 2번째 자리에서 반올림합니다.
mapView.setZoomLevel()
//Map의 Zoom 사용 여부를 알 수 있습니다
mapView.getUseZoomGesture()
//Map의 Zoom  사용 여부를 정의합니다.
mapView.setUseZoomGesture()
//Map의 Rotate 사용 여부를 알 수 있습니다.
mapView.getUseRotateGesture()
//Map의 Rotate  사용 여부를 정의합니다.
mapView.setUseRotateGesture()

//Map을 원하는 각도만큼 회전시킵니다.
mapView.setAngle()
//3D Map을 X축 기준으로 원하는 각도만큼 기울입니다.
mapView.setMapTilt()
```

> 지도 이동/회전

```kotlin
// 지정된 좌표로 지도중심점을 이동시킵니다.
mapView.moveTo(x: Double, y: Double, animate: Boolean)
// 지정된 각도만큼 지도를 회전시킵니다.
mapView.setAngle(angle: Double, animate: Boolean)
```


> 층 변경하기

```kotlin
// 지도의 층을 변경합니다.
// floorLevel 값은 FloorInfo, Poi 데이터에 포함되어 있습니다. 
mapView.setFloor(floorLevel)
```

> 지도 모드 변경

```kotlin
// 지도를 2D/3D 모드로 전환합니다.
// true : 2D 모드
// false : 3D 모드
mapView.setCameraMode(true);
```

</details>

<details>
<summary>마커 표시하기</summary>

### 지도 내에 다양한 마커를 추가/삭제하는 방법을 설명합니다.

> 사용자 정의 마커

```kotlin
// 리소스를 이용하여 지도에 사용자 정의 마커를 추가합니다.
mapView.addMarker(R.drawable.marker, x, y, floorLevel)
```

> 마커 제거

```kotlin
// 지도상에 있는 모든 마커를 제거합니다.
mapView.removeMarker()
```
</details>

<details>
<summary>길찾기 요청</summary>

### 출/도착지 및 경유지를 설정하여 길찾기를 요청합니다.

> Callback 을 생성합니다.

```kotlin
private val listener = object : IMNavigationListener {
        override fun onPathResult(pathResult: PathResult) {
            // 길찾기 요청이 완료되었을 때 호출됩니다.
            // 탐색된 Path 데이터가 전달됩니다.
        }

        override fun onStart() {
            // 내비게이션이 시작될 때 호출됩니다.
        }

        override fun onFinish() {
            // 내비게이션이 완료되었을 때 호출됩니다.
        }

        override fun onCancel() {
            // 내비게이션을 취소하였을 때 호출됩니다.
        }

        override fun onUpdate(
            currentRoute: Route,
            currentPath: Path,
            currentNodeData: NodeData,
            snapPosition: Vector3
        ) {
            // 내비게이션이 진행되고 있을 때 매번 호출됩니다.
            // 현재 진행중인 Path 데이터가 전달됩니다.
        }

        override fun onRescan() {
            // 내비게이션이 진행 중 재탐색이 되었을 때 호출됩니다.
        }

        override fun onError(e: IMError?) {
            // 길찾기 요청을 실패하였을 때 호출됩니다.
            // 에러메시지가 전달됩니다.
        }
    }
```


    
> 시작위치와 도착위치 및 이동수단을 지정하여 길찾기를 요청합니다.

```kotlin
// 출발 위치와 해당 층을 설정합니다.   
        val startLocation = Location(
            Vector3(startData.x, startData.y, 0.0),
            startData.floorLevel,
            "시작"
        )
        val endLocation = Location(
            Vector3(endData.x, endData.y, 0.0),
            endData.floorLevel,
            "도착"
        )
        val request =
            PathRequest(
                startLocation,
                endLocation,
                mutableListOf(),
                TransType.ALL
            )
        
        mapView.findPath(request, navigationListener)
```
</details>


