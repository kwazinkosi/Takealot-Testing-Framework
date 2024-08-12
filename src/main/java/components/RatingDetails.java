package components;

/**
 * Represents the rating details of a product.
 * This class holds both the rating value and the number of raters.
 */
public class RatingDetails {
    private final float rating;
    private final int ratersCount;

    /**
     * Constructor to initialize the rating details.
     * 
     * @param rating The rating value.
     * @param ratersCount The number of raters.
     */
    public RatingDetails(float rating, int ratersCount) {
        this.rating = rating;
        this.ratersCount = ratersCount;
    }

    /**
     * Gets the rating value.
     * 
     * @return The rating value as a float.
     */
    public float getRating() {
        return rating;
    }

    /**
     * Gets the number of raters.
     * 
     * @return The number of raters as an int.
     */
    public int getRatersCount() {
        return ratersCount;
    }
}
