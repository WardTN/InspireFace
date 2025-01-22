package com.solexcv.facelocatesdk.core;

import android.graphics.Bitmap;

public class FaceLocateCore {

    static {
        System.loadLibrary("facelocate_0.1.1");
    }

    native long CreateFaceLocatePredictor(String modelFolderPath);

    native void ReleaseFaceLocatePredictor(long handle);

    native int PredictFaceInImg(long handle, int[] rgba, int height, int width);

    native int PredictFaceInImgNV21(long handle, byte[] yuv, int height, int width);

    native String GetFaceRatioAndOffest(long handle, int[] rgba, int height, int width);

    native String GetFaceRatioAndOffestNV21(long handle, byte[] yuv, int height, int width);

    native String GetFaceRectAndLine(long handle, int[] rgba, int height, int width);

    native String GetFaceRectAndLineNV21(long handle, byte[] yuv, int height, int width);

    native String GetDetectRegion(long handle, int[] rgba, int height, int width);

    native String GetDetectRegionNV21(long handle, byte[] yuv, int height, int width,int current_region,float iou_thresh);

    native float GetTextureScore(long handle, int[] rgba, int height, int width);

    // =====================Implement====================

    private long handler;

    private boolean alreadyLoad = false;

    public void initialization(String packFilePath) {
        handler = CreateFaceLocatePredictor(packFilePath);
        System.out.println("handler: " + handler);
    }

    public void release() {
        if (alreadyLoad) {
            ReleaseFaceLocatePredictor(handler);
        }
        alreadyLoad = false;
    }


    public int predictFaceInImg(Bitmap image) {
        int mWidth = image.getWidth();
        int mHeight = image.getHeight();
        int[] argb = new int[image.getWidth() * image.getHeight()];
        image.getPixels(argb, 0, mWidth, 0, 0, mWidth, mHeight);

        return PredictFaceInImg(handler, argb, mHeight, mWidth);
    }

    public int predictFaceInImg(byte[] yuv, int height, int width) {
        return PredictFaceInImgNV21(handler, yuv, height, width);
    }

    public String getFaceRatioAndOffest(Bitmap image) {
        int mWidth = image.getWidth();
        int mHeight = image.getHeight();
        int[] argb = new int[image.getWidth() * image.getHeight()];
        image.getPixels(argb, 0, mWidth, 0, 0, mWidth, mHeight);

        return GetFaceRatioAndOffest(handler, argb, mHeight, mWidth);
    }

    public String getFaceRatioAndOffest(byte[] yuv, int height, int width) {
        return GetFaceRatioAndOffestNV21(handler, yuv, height, width);
    }

    public String getFaceRectAndLine(Bitmap image) {
        int mWidth = image.getWidth();
        int mHeight = image.getHeight();
        int[] argb = new int[image.getWidth() * image.getHeight()];
        image.getPixels(argb, 0, mWidth, 0, 0, mWidth, mHeight);

        return GetFaceRectAndLine(handler, argb, mHeight, mWidth);
    }

    public String getFaceRectAndLine(byte[] yuv, int height, int width) {
        return GetFaceRectAndLineNV21(handler, yuv, height, width);
    }


    public String getDetectRegion(Bitmap image) {
        int mWidth = image.getWidth();
        int mHeight = image.getHeight();
        int[] argb = new int[image.getWidth() * image.getHeight()];
        image.getPixels(argb, 0, mWidth, 0, 0, mWidth, mHeight);

        return GetDetectRegion(handler, argb, mHeight, mWidth);
    }

    public String getDetectRegion(byte[] yuv, int height, int width,int current_region,float iou_thresh) {
        return GetDetectRegionNV21(handler, yuv, height, width,current_region,iou_thresh);
    }

    public float getTextureScore(Bitmap image) {
        int mWidth = image.getWidth();
        int mHeight = image.getHeight();
        int[] argb = new int[image.getWidth() * image.getHeight()];
        image.getPixels(argb, 0, mWidth, 0, 0, mWidth, mHeight);

        return GetTextureScore(handler, argb, mHeight, mWidth);
    }


}
