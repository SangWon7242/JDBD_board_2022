package com.psw.exam.board.util;

import java.util.HashMap;
import java.util.Map;

public class Util {
  public static Map<String, String> getParamsFromUrl(String url) {
    Map<String, String> params = new HashMap<>();

    String[] urlBits = url.split("\\?", 2);

    if (urlBits.length == 1) {
      return params;
    }

    for (String bit : urlBits[1].split("&")) {
      String[] bitBits = bit.split("=", 2);

      if (bitBits.length == 1) {
        continue;
      }

      params.put(bitBits[0], bitBits[1]);
    }

    return params;
  }

  public static String getUrlPathFromUrl(String url) {
    return url.split("\\?", 2)[0];
  }

}
