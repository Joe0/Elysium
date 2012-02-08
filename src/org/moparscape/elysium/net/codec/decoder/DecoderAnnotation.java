package org.moparscape.elysium.net.codec.decoder;

import java.lang.annotation.*;

/**
 * Decoders that should be treated differently should have this annotation.
 * 
 * @author Joe Pritzel
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ ElementType.TYPE })
public @interface DecoderAnnotation {

	/**
	 * 
	 * @return If this Decoder should be ignored when initializing the
	 *         CodecLookupService.
	 */
	public boolean shouldIgnore();
}
