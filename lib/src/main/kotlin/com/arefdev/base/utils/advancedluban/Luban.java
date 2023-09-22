/*Copyright 2016 Zheng Zibin

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package com.arefdev.base.utils.advancedluban;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.IntDef;

import java.io.File;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import timber.log.Timber;

public class Luban {

	public static final int FIRST_GEAR = 1;

	public static final int THIRD_GEAR = 3;

	public static final int CUSTOM_GEAR = 4;

	private static final String TAG = "Luban";

	private static final String DEFAULT_DISK_CACHE_DIR = "luban_disk_cache";

	private File mFile;

	private List<File> mFileList;

	private final LubanBuilder mBuilder;

	private Luban(File cacheDir) {
		mBuilder = new LubanBuilder(cacheDir);
	}

	public static Luban compress(Context context, File file) {
		Luban luban = new Luban(Luban.getPhotoCacheDir(context));
		luban.mFile = file;
		luban.mFileList = Collections.singletonList(file);
		return luban;
	}

	public static Luban compress(Context context, List<File> files) {
		Luban luban = new Luban(Luban.getPhotoCacheDir(context));
		luban.mFileList = new ArrayList<>(files);
		luban.mFile = files.get(0);
		return luban;
	}

	/**
	 * @param file     要压缩的单个文件
	 * @param cacheDir 压缩完文件的存储路径
	 */
	public static Luban compress(File file, File cacheDir) {
		if (!isCacheDirValid(cacheDir)) {
			throw new IllegalArgumentException("The cacheDir must be Directory");
		}
		Luban luban = new Luban(cacheDir);
		luban.mFile = file;
		luban.mFileList = Collections.singletonList(file);
		return luban;
	}

	public static Luban compress(List<File> files, File cacheDir) {
		if (!isCacheDirValid(cacheDir)) {
			throw new IllegalArgumentException("The cacheDir must be Directory");
		}
		Luban luban = new Luban(cacheDir);
		luban.mFile = files.get(0);
		luban.mFileList = new ArrayList<>(files);
		return luban;
	}

	private static boolean isCacheDirValid(File cacheDir) {
		return cacheDir.isDirectory() && (cacheDir.exists() || cacheDir.mkdirs());
	}

	/**
	 * 自定义压缩模式 FIRST_GEAR、THIRD_GEAR、CUSTOM_GEAR
	 */
	public Luban putGear(@GEAR int gear) {
		mBuilder.gear = gear;
		return this;
	}

	/**
	 * 自定义图片压缩格式
	 */
	public Luban setCompressFormat(Bitmap.CompressFormat compressFormat) {
		mBuilder.compressFormat = compressFormat;
		return this;
	}

	/**
	 * CUSTOM_GEAR 指定目标图片的最大体积
	 */
	public Luban setMaxSize(int size) {
		mBuilder.maxSize = size;
		return this;
	}

	/**
	 * CUSTOM_GEAR 指定目标图片的最大宽度
	 *
	 * @param width 最大宽度
	 */
	public Luban setMaxWidth(int width) {
		mBuilder.maxWidth = width;
		return this;
	}

	/**
	 * CUSTOM_GEAR 指定目标图片的最大高度
	 *
	 * @param height 最大高度
	 */
	public Luban setMaxHeight(int height) {
		mBuilder.maxHeight = height;
		return this;
	}

	/**
	 * listener调用方式，在主线程订阅并将返回结果通过 listener 通知调用方
	 *
	 * @param listener 接收回调结果
	 */
	public void launch(final OnCompressListener listener) {
		//noinspection ResultOfMethodCallIgnored
		asObservable().observeOn(AndroidSchedulers.mainThread()).doOnSubscribe(
						disposable -> listener.onStart())
				.subscribe(listener::onSuccess, listener::onError);
	}

	/**
	 * listener调用方式，在主线程订阅并将返回结果通过 listener 通知调用方
	 *
	 * @param listener 接收回调结果
	 */
	public void launch(final OnMultiCompressListener listener) {
		//noinspection ResultOfMethodCallIgnored
		asListObservable().observeOn(AndroidSchedulers.mainThread())
				.doOnSubscribe(disposable -> listener.onStart())
				.subscribe(listener::onSuccess, listener::onError);
	}

	/**
	 * 返回File Observable
	 */
	public Observable<File> asObservable() {
		LubanCompresser compressor = new LubanCompresser(mBuilder);
		return compressor.singleAction(mFile);
	}

	/**
	 * 返回fileList Observable
	 */
	public Observable<List<File>> asListObservable() {
		LubanCompresser compressor = new LubanCompresser(mBuilder);
		return compressor.multiAction(mFileList);
	}

	// Utils

	/**
	 * Returns a directory with a default name in the private cache directory of the application to
	 * use to store
	 * retrieved media and thumbnails.
	 *
	 * @param context A context.
	 * @see #getPhotoCacheDir(Context, String)
	 */
	private static File getPhotoCacheDir(Context context) {
		return getPhotoCacheDir(context, Luban.DEFAULT_DISK_CACHE_DIR);
	}

	/**
	 * Returns a directory with the given name in the private cache directory of the application to
	 * use to store
	 * retrieved media and thumbnails.
	 *
	 * @param context   A context.
	 * @param cacheName The name of the subdirectory in which to store the cache.
	 * @see #getPhotoCacheDir(Context)
	 */
	private static File getPhotoCacheDir(Context context, String cacheName) {
		File cacheDir = context.getCacheDir();
		if (cacheDir != null) {
			File result = new File(cacheDir, cacheName);
			if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
				// File wasn't able to create a directory, or the result exists but not a directory
				return null;
			}
			return result;
		}
		if (Log.isLoggable(TAG, Log.ERROR)) {
			Timber.tag(TAG).e("default disk cache dir is null");
		}
		return null;
	}

	/**
	 * 清空Luban所产生的缓存
	 * Clears the cache generated by Luban
	 */
	public Luban clearCache() {
		if (mBuilder.cacheDir.exists()) {
			deleteFile(mBuilder.cacheDir);
		}
		return this;
	}

	/**
	 * 清空目标文件或文件夹
	 * Empty the target file or folder
	 */
	private void deleteFile(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory()) {
			for (File file : fileOrDirectory.listFiles()) {
				deleteFile(file);
			}
		}
		fileOrDirectory.delete();
	}

	@IntDef({FIRST_GEAR, THIRD_GEAR, CUSTOM_GEAR})
	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.SOURCE)
	@Documented
	@Inherited
	@interface GEAR {
	}
}