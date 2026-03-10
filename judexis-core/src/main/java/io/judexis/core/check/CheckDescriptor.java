package io.judexis.core.check;

/**
 * Immutable registration descriptor for a check.
 */
public final class CheckDescriptor {
    private final String id;
    private final CheckCategory category;
    private final Check check;

    public CheckDescriptor(String id, CheckCategory category, Check check) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id is required");
        }
        if (category == null) {
            throw new IllegalArgumentException("category is required");
        }
        if (check == null) {
            throw new IllegalArgumentException("check is required");
        }
        this.id = id;
        this.category = category;
        this.check = check;
    }

    public String getId() {
        return id;
    }

    public CheckCategory getCategory() {
        return category;
    }

    public Check getCheck() {
        return check;
    }
}
