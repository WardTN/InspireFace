package com.example.inspireface_example.bean;

import java.util.List;

public class FaceRectAndLine {
    private FaceBoxBean face_box; // 人脸矩形框
    private FaceLineBean face_line;
    private List<List<Integer>> face_outline_points;  // 人脸外轮廓
    private List<List<List<Integer>>> face_six_region; // 六区域轮廓

    public FaceBoxBean getFace_box() {
        return face_box;
    }

    public void setFace_box(FaceBoxBean face_box) {
        this.face_box = face_box;
    }

    public FaceLineBean getFace_line() {
        return face_line;
    }

    public void setFace_line(FaceLineBean face_line) {
        this.face_line = face_line;
    }

    public List<List<Integer>> getFace_outline_points() {
        return face_outline_points;
    }

    public void setFace_outline_points(List<List<Integer>> face_outline_points) {
        this.face_outline_points = face_outline_points;
    }

    public List<List<List<Integer>>> getFace_six_region() {
        return face_six_region;
    }


    public int getAllNum() {
        int totalNum = 0;

        int tol1 = face_six_region.get(0).size();
        int tol2 = face_six_region.get(1).size();
        int tol3 = face_six_region.get(2).size();
        int tol4 = face_six_region.get(3).size();
        int tol5 = face_six_region.get(4).size();
        int tol6 = face_six_region.get(5).size();

        totalNum += tol1 + tol2 + tol3 + tol4 + tol5 + tol6;

        return totalNum;
    }


    public void setFace_six_region(List<List<List<Integer>>> face_six_region) {
        this.face_six_region = face_six_region;
    }

    public static class FaceBoxBean {
        private List<Integer> left_up;
        private List<Integer> right_down;

        public List<Integer> getLeft_up() {
            return left_up;
        }

        public void setLeft_up(List<Integer> left_up) {
            this.left_up = left_up;
        }

        public List<Integer> getRight_down() {
            return right_down;
        }

        public void setRight_down(List<Integer> right_down) {
            this.right_down = right_down;
        }
    }

    public static class FaceLineBean {
        private List<List<Integer>> line1;
        private List<List<Integer>> line2;
        private List<List<Integer>> line3;

        public List<List<Integer>> getLine1() {
            return line1;
        }

        public void setLine1(List<List<Integer>> line1) {
            this.line1 = line1;
        }

        public List<List<Integer>> getLine2() {
            return line2;
        }

        public void setLine2(List<List<Integer>> line2) {
            this.line2 = line2;
        }

        public List<List<Integer>> getLine3() {
            return line3;
        }

        public void setLine3(List<List<Integer>> line3) {
            this.line3 = line3;
        }
    }
}
