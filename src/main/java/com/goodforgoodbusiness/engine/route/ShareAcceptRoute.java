package com.goodforgoodbusiness.engine.route;

import org.apache.log4j.Logger;

import com.goodforgoodbusiness.engine.crypto.PointerCrypter;
import com.goodforgoodbusiness.engine.crypto.primitive.key.EncodeableShareKey;
import com.goodforgoodbusiness.engine.store.keys.spec.ShareKeySpec;
import com.goodforgoodbusiness.shared.encode.JSON;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import spark.Request;
import spark.Response;
import spark.Route;

@Singleton
public class ShareAcceptRoute implements Route {	
	private static final Logger log = Logger.getLogger(ShareAcceptRoute.class);
	
	public static class ShareAcceptRequest {
		@Expose
		@SerializedName("pattern")
		private ShareKeySpec spec;
		
		@Expose
		@SerializedName("key")
		private EncodeableShareKey key;
	}
	
	private final PointerCrypter pointerCrypter;
	
	@Inject
	public ShareAcceptRoute(PointerCrypter pointerCrypto) {
		this.pointerCrypter = pointerCrypto;
	}
	
	@Override
	public Object handle(Request req, Response res) throws Exception {		
		log.info("Processing claim post");
		
		var sar = JSON.decode(req.body(), ShareAcceptRequest.class);
		if (sar != null && sar.key != null) {
			pointerCrypter.saveShareKey(sar.spec, sar.key);
		}
		
		return "OK";
	}
}
