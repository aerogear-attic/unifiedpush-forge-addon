package org.jboss.aerogear.commands;

import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.templates.TemplateFactory;
import org.jboss.forge.addon.templates.freemarker.FreemarkerTemplate;
import org.jboss.forge.addon.ui.command.AbstractUICommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;

import java.lang.Override;
import java.lang.Exception;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class UnfiedPushGenerateService extends AbstractProjectCommand {

	@Inject
	private ResourceFactory resourceFactory;

	@Inject
	private ProjectFactory projectFactory;

	@Inject
	private TemplateFactory templateFactory;

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(UnfiedPushGenerateService.class)
				.name("UnfiedPushGenerateService")
				.category(Categories.create("unifiedpush"));
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		Resource<?> interfaceResource = resourceFactory.create(getClass()
				.getResource("/org/jboss/aerogear/service/SenderService.ftl"));
		org.jboss.forge.addon.templates.Template interfaceTemplate = templateFactory
				.create(interfaceResource, FreemarkerTemplate.class);
		JavaSourceFacet javaFacet = getSelectedProject(context).getFacet(JavaSourceFacet
				.class);
	
		Map<String, Object> params = new HashMap<String, Object>(); 
		params.put("package", javaFacet.getBasePackage()+ ".service");
		String output = interfaceTemplate.process(params);
//		
//		destinationResource.createNewFile();
//		destinationResource.setContents(output);

		return Results
				.success("Command 'UnfiedPushGenerateService' successfully executed!");
	}

	@Override
	protected boolean isProjectRequired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected ProjectFactory getProjectFactory() {
		// TODO Auto-generated method stub
		return projectFactory;
	}
}