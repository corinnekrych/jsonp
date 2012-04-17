
import grails.converters.JSON;

import grails.jsonp.JSONP

class JsonpGrailsPlugin {
	// the plugin version
	def version = "0.3"
	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "2.0 > *"

	// resources that are excluded from plugin packaging
	def pluginExcludes = [
		"grails-app/views/error.gsp"
	]


	// the other plugins this plugin depends on
	def dependsOn = [controllers: '1.1 > *']
	def loadAfter = ['controllers']
	def observe = ['controllers']

	// TODO Fill in these fields
	def title = "Jsonp Plugin" // Headline display name of the plugin
	def author = "Your name"
	def authorEmail = ""
	def description = '''\
Override render method defined for all controller to add parameter callback 
function name to provide cross domain JSONP RESTfull controllers
'''

	// URL to the plugin's documentation
	def documentation = "http://grails.org/plugin/jsonp"

	// Extra (optional) plugin metadata

	// License: one of 'APACHE', 'GPL2', 'GPL3'
	def license = "APACHE"

	// Details of company behind the plugin (if there is one)
	//def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

	// Any additional developers beyond the author specified above.
	def developers = [
		[ name: "Corinne Krych", email: "corinnekrych@gmail.com" ],
		[ name: "Fabrice Matrat", email: "fabricematrat@gmail.com" ],
		[ name: "Sebastien Blanc", email: "scm.blanc@gmail.com" ]
	]

	// Location of the plugin's issue tracker.
	//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

	// Online location of the plugin's browseable source code.
	//    def scm = [ url: "http://svn.grails-plugins.codehaus.org/browse/grails-plugins/" ]

	def doWithWebDescriptor = { xml ->
		// TODO Implement additions to web.xml (optional), this event occurs before
	}

	def doWithSpring = {
		// TODO Implement runtime spring config (optional)

	}

	def doWithDynamicMethods = { ctx ->

		application.controllerClasses.each { controller ->
			//def methods = controller.metaClass.getMetaMethods()
			//def methods = controller.metaClass.getExpandoMethods()
			def methods = controller.metaClass.getMethods()
//			methods.each {
//				if (it.name == 'render') {
//					log.error it.signature
//				}
//			}
			def original = controller.metaClass.pickMethod("render", [
				org.codehaus.groovy.grails.web.converters.Converter
			]as Class[])
			log.error "...$original"
			controller.metaClass.render = {JSON arg ->
				
				log.error 'before invoke'
				def jsonp = new JSONP(response, params.callback, arg.target)			
				original.invoke(delegate, jsonp)
				log.error 'after invoke'

			}

		}
	}



	
	
	def doWithApplicationContext = { applicationContext ->
		// TODO Implement post initialization spring config (optional)
		//JSON.registerObjectMarshaller(new JSONPMarshaller(), 1)
	}

	def onChange = { event ->
		// TODO Implement code that is executed when any artefact that this plugin is
		// watching is modified and reloaded. The event contains: event.source,
		// event.application, event.manager, event.ctx, and event.plugin.
	}

	def onConfigChange = { event ->
		// TODO Implement code that is executed when the project configuration changes.
		// The event is the same as for 'onChange'.
	}

	def onShutdown = { event ->
		// TODO Implement code that is executed when the application shuts down (optional)
	}
}
