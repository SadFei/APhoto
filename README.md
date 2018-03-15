# :camera: APhoto
打开相机拍照或打开相册选择图片

* 说明
```
1、打开相机拍照保存的图片在名字为“Trevor”的文件夹。
2、打开相机拍照后的图片会保存为“WEBP”格式的已压缩图片。
```

* Gradle
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
	compile 'com.github.SadFei:APhoto:1.0.3'
```

* Maven
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	
	<dependency>
	    <groupId>com.github.SadFei</groupId>
	    <artifactId>APhoto</artifactId>
	    <version>1.0.2</version>
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
```
3、设置压缩格式
	PhotoAlbum.with(this)
                .openPhotoAlbum()
		 // 设置压缩格式，不设置默认为 Bitmap.CompressFormat.JPEG
		.onCompressFormat(Bitmap.CompressFormat.JPEG)
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

