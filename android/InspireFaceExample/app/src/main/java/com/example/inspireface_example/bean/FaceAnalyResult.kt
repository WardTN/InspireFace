package com.example.inspireface_example.bean

//
data class FaceAnalyResult(var normalResult: NormalResult = NormalResult(), var poseAngle: PoseAngle = PoseAngle(), var imageQuality: ImageQuality = ImageQuality())

data class NormalResult(
    var age: String = "60",
    var sex: Int = 1,
    var smilePercent: Float = 50f,
    var leftEyeState:String = "睁眼,未戴眼镜",
    var rightEyeState:String = "睁眼,未戴眼镜"
)

data class PoseAngle(
    var angle:Float = 20f
)

data class ImageQuality(
    var quality:Float = 20f
)






