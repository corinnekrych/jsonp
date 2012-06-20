package grails.jsonp

import grails.converters.JSON;

import java.io.Writer;

import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException;
import org.codehaus.groovy.grails.web.sitemesh.GrailsContentBufferingResponse;


class JSONP extends JSON {
  def response
  def callback
  def target

  JSONP(response, callback, target) {
    this.response = response
    this.callback = callback
    this.target = target
  }

  void render(Writer out) {
    if (target!= null && (target instanceof ArrayList && target.size == 0)) {
      response.getWriter().write(callback + '(')
      response.getWriter().write(')')
    } else {
      response.getWriter().write(callback + '(')
      prepareRender(out)
      try {
        value(target)
        response.getWriter().write(')')
      }
      finally {
        finalizeRender(out)
      }
    }
  }

}