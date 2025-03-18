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
  public String getIpAddr() {
    String ip_addr = null;
    ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = sra.getRequest();

    ip_addr = request.getHeader("X-Forwarded-For");
    if (ip_addr == null) {
      ip_addr = request.getHeader("Proxy-Client-IP");
    }
    if (ip_addr == null) {
      ip_addr = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip_addr == null) {
      ip_addr = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip_addr == null) {
      ip_addr = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip_addr == null) {
      ip_addr = request.getRemoteAddr();
    }
    return ip_addr;
  }
}
