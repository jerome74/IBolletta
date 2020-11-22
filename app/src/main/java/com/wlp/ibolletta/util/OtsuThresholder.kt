package com.wlp.ibolletta.util

class OtsuThresholder
{
    lateinit var histData : IntArray
    var maxLevelValue : Int = 0
    var threshold : Int = 0


    init {
        histData = IntArray(256)
    }

    fun doThreshold( srcData : ByteArray) : Int
    {
        var ptr = 0

        // Clear histogram data
        // Set all values to zero
        while (ptr < histData.size) histData[ptr++] = 0

        // Calculate histogram and find the level with the max value
        // Note: the max level value isn't required by the Otsu method
        ptr = 0;

        while (ptr < srcData.size)
        {
            val h = 0xFF and srcData[ptr].toInt()
            histData[h] ++;
            if (histData[h] > maxLevelValue) maxLevelValue = histData[h];
            ptr ++;
        }

        // Total number of pixels
        var total = srcData.size;

        var sum : Float = 0f;

        for (t in 0 until 256) sum += t * histData[t];

        var sumB : Float = 0f;
        var wB = 0;
        var wF = 0;

        var varMax : Float = 0f;
        threshold = 0;

        for (t in 0 until 256)
        {
            wB += histData[t];					// Weight Background
            if (wB == 0) continue;

            wF = total - wB;						// Weight Foreground
            if (wF == 0) break;

            sumB += (t * histData[t]).toFloat()

            var mB : Float = sumB / wB;				// Mean Background
            var mF : Float  = (sum - sumB) / wF;		// Mean Foreground

            // Calculate Between Class Variance
            var varBetween = wB.toFloat() * wF.toFloat() * (mB - mF) * (mB - mF);

            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }

        return threshold;

    }

}