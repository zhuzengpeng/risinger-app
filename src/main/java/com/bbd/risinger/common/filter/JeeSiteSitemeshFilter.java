package com.bbd.risinger.common.filter;

import com.opensymphony.sitemesh.webapp.SiteMeshFilter;


import javax.servlet.annotation.WebFilter;
@WebFilter(urlPatterns = {"/a/*", "/f/*"})
public class JeeSiteSitemeshFilter extends SiteMeshFilter {
}
