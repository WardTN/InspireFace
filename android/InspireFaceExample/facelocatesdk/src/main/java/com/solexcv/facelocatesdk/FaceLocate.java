package com.solexcv.facelocatesdk;

import android.content.Context;
import android.graphics.Bitmap;

import com.solexcv.facelocatesdk.core.FaceLocateCore;

public class FaceLocate {

    private FaceLocateCore core;

    final private String modelsFolderName = "face_location_models";

    public FaceLocate(Context context) {
        String resourceFolderPath = context.getExternalFilesDir(null).getAbsolutePath() + "/";
        FaceLocateUtils.copyFilesFromAssets(context, modelsFolderName, resourceFolderPath + modelsFolderName);
        core = new FaceLocateCore();
        System.out.println("model: "+ resourceFolderPath + modelsFolderName);
        core.initialization(resourceFolderPath + modelsFolderName);
    }

    /**
     * 检查当前有没有人脸
     * */
    public int predictFaceInImg(Bitmap bitmap) {
        return core.predictFaceInImg(bitmap);
    }

    /**
     * 检查当前有没有人脸 - NV21
     * */
    public int predictFaceInImg(byte[] yuv, int height, int width) {
        return core.predictFaceInImg(yuv, height, width);
    }

    /**
     * 获取人脸在图像中的面积比例，以及相对中心点的偏移值
     * */
    public String getFaceRatioAndOffest(Bitmap bitmap) {
        return core.getFaceRatioAndOffest(bitmap);
    }

    /**
     * 获取人脸在图像中的面积比例，以及相对中心点的偏移值 - NV21
     * */
    public String getFaceRatioAndOffest(byte[] yuv, int height, int width) {
        return core.getFaceRatioAndOffest(yuv, height, width);
    }

    /**
     * 获取人脸的box框和三条线、外轮廓位置
     * */
    public String getFaceRectAndLine(Bitmap bitmap) {
        return core.getFaceRectAndLine(bitmap);
    }


    /**
     * 获取人脸的box框和三条线、外轮廓位置 - NV21
     * */
    public String getFaceRectAndLine(byte[] yuv, int height, int width) {
        return core.getFaceRectAndLine(yuv, height, width);
    }

    /**
     * 获取检测到的位置
     * */
    public String getDetectRegion(Bitmap bitmap) {
        return core.getDetectRegion(bitmap);
    }

    /**
     * 获取检测到的位置 - NV21
     * */
    public String getDetectRegion(byte[] yuv, int height, int width,int current_region,float iou_thresh) {
        return core.getDetectRegion(yuv, height, width,current_region,iou_thresh);
    }

    /**
     * 局部测肤纹理得分
     * */
    public float getTextureScore(Bitmap bitmap) {
        return core.getTextureScore(bitmap);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        core.release();
    }
}
