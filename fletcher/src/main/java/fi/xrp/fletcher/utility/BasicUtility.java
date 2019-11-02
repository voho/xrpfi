package fi.xrp.fletcher.utility;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import lombok.NonNull;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class BasicUtility {
    private static final HashFunction SHA_256 = Hashing.sha256();

    @NonNull
    public static String hash(@NonNull final String... parts) {
        final Hasher hasher = SHA_256.newHasher();
        Arrays.stream(parts).forEach(part -> hasher.putString(part, StandardCharsets.UTF_8));
        return hasher.hash().toString();
    }

    @Nullable
    public static <T> T coalesce(@Nullable final T... values) {
        if (values != null && values.length > 0) {
            for (final T value : values) {
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }
}
