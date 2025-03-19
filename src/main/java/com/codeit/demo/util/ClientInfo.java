package com.codeit.demo.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class ClientInfo {

  /**
   * 클라이언트의 ip 주소를 가져오기
   *
   * @return ip 주소
   */
  public static String getIpAddr() {
    ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = sra.getRequest();

    String ip = getIpFromHeaders(request);
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }

    // IPV6(::1) 주소 -> IPv4 주소(127.0.0.1)로 변환
    if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
      ip = "127.0.0.1";
    }

    return ip;
  }

  private static String getIpFromHeaders(HttpServletRequest request) {
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(
        xForwardedFor)) {
      String[] ips = xForwardedFor.split(",");
      for (String candidateIp : ips) {
        candidateIp = candidateIp.trim();
        if (isIPv4(candidateIp)) {
          return candidateIp; // IPv4 주소 형식으로 반환
        }
      }
    }

    String[] headers = {
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_CLIENT_IP",
        "HTTP_X_FORWARDED_FOR"
    };

    for (String header : headers) {
      String ip = request.getHeader(header);
      if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
        if (isIPv4(ip)) {
          return ip;
        }
      }
    }

    return null;
  }

  private static boolean isIPv4(String ip) {
    return ip.matches("\\d+\\.\\d+\\.\\d+\\.\\d+");
  }
}
