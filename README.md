# HTLiveData

DM Kiên dùng đi ngon lắm

```Sample
private val testLiveData = SingleStateLiveData<User>() //observe 1 lần
//private val testLiveData = StateLiveData<User>() //Observe 1 nhiều lần
```


```
override fun onCreate(savedInstanceState: Bundle?) {
    super.onResume()
    testLiveData.observeSuccess(this) { user ->
    
        Log.d("MainActivity", "onResume (line 25): $user")
        
    }.observeFailed { errorCode ->
    
        Log.d("MainActivity", "onResume (line 27): $errorCode")
        
    }.observeLoading { isLoading, progress ->
    
        Log.d("MainActivity", "onResume (line 29): $isLoading $progress")
        
    }
} 
```        

add to project
```
allprojects {
repositories {
 ...
    maven { url 'https://jitpack.io' }
  }
}
```

```
dependencies {
  implementation 'com.github.haitrvnvn:HTLiveData:0.0.2b'
}
```  
