# :camera: APhoto
打开相机拍照或打开相册选择图片

* 说明
```
1、打开相机拍照保存的图片在私有文件夹;文件夹名字为“Trevor”
2、打开相机拍照后的图片根据当前手机系统版本的不同，会保存为不同格式（level > 14 : WEBP；< 14 JPG）的已压缩图片。
```

* Gradle
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
implementation 'com.github.SadFei:PhotoAlbum:v1.0'
```

* Maven
```
<dependency>
	    <groupId>com.github.SadFei</groupId>
	    <artifactId>PhotoAlbum</artifactId>
	    <version>v1.0</version>
	</dependency>
```


* 使用方式
```
1、打开相机
	PhotoAlbum.with(this)
                .openCamera()
                .onResult(new ActionResult() {
                    @Override
                    public void onFile(File file) {
                        imageViewTwo.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    }
                })
                .start();
```
```
2、打开系统相册
	PhotoAlbum.with(this)
                .openPhotoAlbum()
                .onResult(new ActionResult() {
                    @Override
                    public void onFile(File file) {
                        imageViewTwo.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    }
                })
                .start();
```
* `警告`
```
在调用之前请确保以获得一下系统权限：
1、android.permission.CAMERA
2、android.permission.READ_EXTERNAL_STORAGE
3、android.permission.WRITE_EXTERNAL_STORAGE
```
这里推荐一个权限请求框架，已尽九牛二虎之力适配了大部分国产手机： [AndPermission](https://github.com/yanzhenjie/AndPermission):+1: 

