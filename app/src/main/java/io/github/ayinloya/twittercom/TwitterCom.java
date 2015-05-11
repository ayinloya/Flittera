package io.github.ayinloya.twittercom;

import android.util.Base64;

import com.parse.ParseTwitterUtils;
import com.parse.signpost.OAuth;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Formatter;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by barnabas on 5/3/15.
 *
 */
public class TwitterCom {
    public static String OAUTH_SIGNATURE_METHOD = "HMAC-SHA1";

    public static String uuid_string;
    // get the timestamp
    static Calendar tempcal = Calendar.getInstance();
    static long ts = tempcal.getTimeInMillis();// get current time in milliseconds
    static String oauth_timestamp = (Long.valueOf(ts / 1000)).toString(); // then divide by 1000 to get seconds
    // the parameter string must be in alphabetical order, "text" parameter added at end
    static String parameter_string = "oauth_consumer_key=" + ParseTwitterUtils.getTwitter().getConsumerKey() + "&oauth_nonce=" + getOnonce() + "&oauth_signature_method=" + OAUTH_SIGNATURE_METHOD +
                                             "&oauth_timestamp=" + oauth_timestamp + "&oauth_token=" + OAuth.percentEncode(ParseTwitterUtils.getTwitter().getAuthToken()) + "&oauth_version=1.0";

    private static String authorization_header_string = "OAuth realm = \"https://api.twitter.com/1.1/followers/list.json\", screen_name = \"" + ParseTwitterUtils.getTwitter().getScreenName() + "\", oauth_consumer_key = \"" + ParseTwitterUtils.getTwitter().getConsumerKey() + "\", oauth_token = \"" + ParseTwitterUtils.getTwitter().getAuthToken() + "\", oauth_signature_method = \"HMAC-SHA1\", oauth_timestamp = \"" +
                                                                oauth_timestamp + "\", oauth_nonce = \"" + getOnonce() + "\", oauth_version = \"1.0\", oauth_signature = \"" + getOauth_signature() + "\"";


    private static String getOnonce() {
        uuid_string = UUID.randomUUID().toString();
        uuid_string = uuid_string.replaceAll("-", "");
        return uuid_string;
    }

    public static String getParameter_string() {
        return parameter_string;
    }

    private static String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {

        SecretKey secretKey;

        byte[] keyBytes = keyString.getBytes();
        secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);

        byte[] text = baseString.getBytes();

        return Base64.encodeToString(mac.doFinal(text),Base64.DEFAULT);
    }

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();

        for (byte b : bytes) {
            formatter.format("%02x", b);
        }

        return formatter.toString();
    }

    public static String getOauth_signature() {

        String parameter_string = "oauth_consumer_key=" + ParseTwitterUtils.getTwitter().getConsumerKey() + "&oauth_nonce=" + getOnonce().substring(0,6) + "&oauth_signature_method=" + OAUTH_SIGNATURE_METHOD + "&oauth_timestamp=" + oauth_timestamp + "&oauth_version=1.0";


        // specify the proper twitter API endpoint at which to direct this request
        String twitter_endpoint = "https://api.twitter.com/oauth/request_token";
        String twitter_endpoint_host = "api.twitter.com";
        String twitter_endpoint_path = "/oauth/request_token";

        // assemble the string to be signed. It is METHOD & percent-encoded endpoint & percent-encoded parameter string
        // Java's native URLEncoder.encode function will not work. It is the wrong RFC specification (which does "+" where "%20" should be)...
        // the encode() function included in this class compensates to conform to RFC 3986 (which twitter requires)
        String signature_base_string = "POST" + "&" + OAuth.percentEncode(twitter_endpoint) + "&" + OAuth.percentEncode(parameter_string);

        String oauth_signature = "";
        try {
            oauth_signature = computeSignature(signature_base_string, OAuth.percentEncode(ParseTwitterUtils.getTwitter().getConsumerSecret()) + "&" + OAuth.percentEncode(ParseTwitterUtils.getTwitter().getAuthToken()));  // note the & at the end. Normally the user access_token would go here, but we don't know it yet for request_token
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return oauth_signature.trim();
    }

    public static String getAuthorization_header_string() {
        return authorization_header_string;
    }
}
