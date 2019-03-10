package edu.illinois.cs.cs125.spring2019.mp3.lib;

//import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Image recognition class. Each function takes the JSON returned by the Microsoft Cognitive
 * Services API and extracts some piece of information from it.
 */
public class RecognizePhoto {
    /**
     * Get the image width.
     * @param json - the JSON string returned by the Microsoft Cognitive Services API
     * @return the width of the image or 0 on failure
     */
    public static int getWidth(final String json) {
        if (json == null) {
            return 0;
        }
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();
        if (result.getAsJsonObject("metadata").get("width") == null) {
            return 0;
        } else {
            return result.getAsJsonObject("metadata").get("width").getAsInt();
        }
    }

    /**
     * Get the image height.
     * @param json - the JSON string returned by the Microsoft Cognitive Services API
     * @return the height of the image or 0 on failure
     */
    public static int getHeight(final String json) {
        if (json == null) {
            return 0;
        }
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();
        if (result.getAsJsonObject("metadata").get("height") == null) {
            return 0;
        } else {
            return result.getAsJsonObject("metadata").get("height").getAsInt();
        }
    }

    /**
     * Get the image file type.
     * @param json - the JSON string returned by the Microsoft Cognitive Services API
     * @return the type of the image or null
     */
    public static String getFormat(final String json) {
        if (json == null) {
            return null;
        }
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();
        if (result.getAsJsonObject("metadata").get("format") == null) {
            return null;
        } else {
            return result.getAsJsonObject("metadata").get("format").getAsString();
        }
    }

    /**
     * Return the caption describing the image created by the Microsoft Cognitive Services API.
     * If multiple captions are provided your code should return the first one.
     * @param json - the JSON string returned by the Microsoft Cognitive Services API
     * @return the caption describing the image created by the Microsoft or null on failure
     */
    public static String getCaption(final String json) {
        if (json == null) {
            return null;
        }
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();
        if (result.getAsJsonObject("description").getAsJsonArray("captions").get(0) == null) {
            return null;
        } else {
            JsonObject asJObject = (JsonObject) result.getAsJsonObject("description").getAsJsonArray("captions").get(0);
            return asJObject.get("text").getAsString();
        }
    }

    /**
     * Determine whether the image contains a dog. You should do this by examining the tags returned
     * by the Cognitive Services API. If a tag with the name "dog" exists with at least the provided
     * confidence, you should return true. Otherwise false.
     * @param json - the JSON string returned by the Microsoft Cognitive Services API
     * @param lowConfidence - the minimum confidence required for this determination
     * @return a boolean indicating whether the image contains a dog or false on failure
     */
    public static boolean isADog(final String json, final double lowConfidence) {
        if (json == null) {
            return false;
        }
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();

        if (result.getAsJsonArray("tags") == null) {
            return false;
        } else {
            for (int i = 0; i < result.getAsJsonArray("tags").size(); i++) {
                JsonObject ithJsonObject = (JsonObject) result.getAsJsonArray("tags").get(i);
                if (ithJsonObject.get("name").getAsString().equals("dog")) {
                    return (ithJsonObject.get("confidence").getAsDouble() >= lowConfidence);
                }
            }
        }
        return false;
    }

    /**
     * Determine whether the image contains a cat. You should do this by examining the tags returned
     * by the Cognitive Services API. If a tag with the name "cat" exists with at least the provided
     * confidence, you should return true. Otherwise false.
     * @param json - the JSON string returned by the Microsoft Cognitive Services API
     * @param lowConfidence - the minimum confidence required for this determination
     * @return a boolean indicating whether the image contains a cat or false on failure
     */
    public static boolean isACat(final String json, final double lowConfidence) {
        if (json == null) {
            return false;
        }
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();

        if (result.getAsJsonArray("tags") == null) {
            return false;
        } else {
            for (int i = 0; i < result.getAsJsonArray("tags").size(); i++) {
                JsonObject ithJsonObject = (JsonObject) result.getAsJsonArray("tags").get(i);
                if (ithJsonObject.get("name").getAsString().equals("cat")) {
                    return (ithJsonObject.get("confidence").getAsDouble() >= lowConfidence);
                }
            }
        }
        return false;
    }

    /**
     * Check if image contains Rick Astley. We leave it to you to determine how to do this by
     * examining the JSON returned for various images. Note that you do not need to consider the
     * confidence value to complete this function.
     * @param json - the JSON returned by the Microsoft Cognitive Services API
     * @return true if you've Rickrolled yourself
     */
    public static boolean isRick(final String json) {
        if (json == null) {
            return false;
        }
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse(json).getAsJsonObject();

        if (result.getAsJsonArray("catagories") == null) {
            return false;
        } else {
            for (int i = 0; i < result.getAsJsonArray("catagories").size(); i++) {
                JsonObject ithJsonObject = (JsonObject) result.getAsJsonArray("catagories").get(i);
                if (ithJsonObject.get("name").getAsString().contains("people")) {
                    for (int j = 0; j < ithJsonObject.getAsJsonObject("detail")
                            .getAsJsonArray("celebrities").size(); j++) {
                        JsonObject celebs = (JsonObject) ithJsonObject.getAsJsonObject("detail")
                                .getAsJsonArray("celebrities").get(j);
                        if (celebs.getAsJsonObject("name").getAsString().equals("Rick Astley")) {
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }
}
