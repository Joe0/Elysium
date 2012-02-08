package org.moparscape.elysium.net.codec;

import org.moparscape.elysium.net.codec.decoder.*;
import org.moparscape.elysium.util.ClassLoadingUtil;

import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * 
 * @author lothy
 */
public final class CodecLookupService {

	private static final List<MessageDecoder<? extends Message>> decoders;

	private static final Map<Class<? extends Message>, MessageDecoder<? extends Message>> decoderMap;

	// Empty constructor to enforce singleton property
	private CodecLookupService() {
	}

	static {
		ImmutableBindingBuilder bindings = new ImmutableBindingBuilder();
		// Initialise all of the decoders in this block

		try {
			// All decoders end with Decoder, so filter all other classes out.
			@SuppressWarnings("unchecked")
			Class<? extends MessageDecoder<? extends Message>>[] classes = (Class<? extends MessageDecoder<? extends Message>>[]) ClassLoadingUtil
					.getClassesInPackage(
							"org.moparscape.elysium.net.codec.decoder",
							"^(.*Decoder)");
			for (Class<? extends MessageDecoder<? extends Message>> c : classes) {
				// Don't include abstract classes or interfaces
				if (!Modifier.isAbstract(c.getModifiers()) && !c.isInterface()) {
					try {
						// Ignore decoders that should be ignored.
						if (((DecoderAnnotation) c
								.getAnnotation(DecoderAnnotation.class))
								.shouldIgnore())
							continue;
					} catch (Exception e) {
					}
					bindings.bindDecoder(c);
				}
			}
		} catch (Exception e) {
			System.out.println("Failed to initialise decoders");
			System.out.println(e);
			throw new ExceptionInInitializerError(e);
		}

		decoders = bindings.decoderList();
		decoderMap = bindings.messageToDecoderMap();
	}

	public static MessageDecoder<? extends Message> getDecoder(int opcode) {
		if (opcode < 0 || opcode >= 255) {
			return null;
		}

		return decoders.get(opcode);
	}

	public static MessageDecoder<? extends Message> getDecoder(
			Class<? extends Message> type) {
		return decoderMap.get(type);
	}

	private static final class ImmutableBindingBuilder {

		private final List<MessageDecoder<? extends Message>> decoders = new ArrayList<MessageDecoder<? extends Message>>(
				255);

		private final Map<Class<? extends Message>, MessageDecoder<? extends Message>> decoderMap = new HashMap<Class<? extends Message>, MessageDecoder<? extends Message>>(
				100, 0.50f);

		public ImmutableBindingBuilder() {
			// Add dummy invalid message handlers to the list.
			// These are replaced for appropriate opcodes.
			InvalidMessageDecoder d = new InvalidMessageDecoder();
			for (int i = 0; i < 255; i++) {
				decoders.add(d);
			}
		}

		public <T extends Message, C extends MessageDecoder<T>> void bindDecoder(
				Class<C> type) throws IllegalAccessException,
				InstantiationException {
			MessageDecoder<T> decoder = type.newInstance();
			int opcode = decoder.getOpcode();

			if (opcode < 0 || opcode >= 255) {
				return;
			}

			this.decoders.set(opcode, decoder);
			this.decoderMap.put(decoder.getMessageType(), decoder);
		}

		public Map<Class<? extends Message>, MessageDecoder<? extends Message>> messageToDecoderMap() {
			return Collections.unmodifiableMap(this.decoderMap);
		}

		public List<MessageDecoder<? extends Message>> decoderList() {
			return Collections.unmodifiableList(this.decoders);
		}
	}
}
