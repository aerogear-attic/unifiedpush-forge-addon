package org.jboss.aerogear.commands;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.templates.Template;
import org.jboss.forge.addon.templates.TemplateFactory;
import org.jboss.forge.addon.templates.freemarker.FreemarkerTemplate;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaInterfaceSource;

public class UnifiedPushGenerateService extends AbstractProjectCommand {

	@Inject
	private ResourceFactory resourceFactory;

	@Inject
	private ProjectFactory projectFactory;

	@Inject
	private TemplateFactory templateFactory;

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(UnifiedPushGenerateService.class)
				.name("unifiedpush: Generate Service")
				.category(Categories.create("unifiedpush"));
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		createServiceInterface(context);
		createServiceImpl(context);

		return Results
				.success("Command 'UnfiedPushGenerateService' successfully executed!");
	}

	private void createServiceInterface(UIExecutionContext context)
			throws IOException {
		Resource<?> interfaceResource = resourceFactory.create(getClass()
				.getResource("/org/jboss/aerogear/service/SenderService.ftl"));
		Template interfaceTemplate = templateFactory.create(interfaceResource,
				FreemarkerTemplate.class);
		JavaSourceFacet javaFacet = getSelectedProject(context).getFacet(
				JavaSourceFacet.class);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("package", javaFacet.getBasePackage());
		String output = interfaceTemplate.process(params);
		JavaInterfaceSource resource = Roaster.parse(JavaInterfaceSource.class,
				output);
		javaFacet.saveJavaSource(resource);
	}

	private void createServiceImpl(UIExecutionContext context)
			throws IOException {
		Resource<?> interfaceResource = resourceFactory.create(getClass()
				.getResource(
						"/org/jboss/aerogear/service/SenderServiceImpl.ftl"));
		Template interfaceTemplate = templateFactory.create(interfaceResource,
				FreemarkerTemplate.class);
		JavaSourceFacet javaFacet = getSelectedProject(context).getFacet(
				JavaSourceFacet.class);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("package", javaFacet.getBasePackage());
		String output = interfaceTemplate.process(params);
		JavaClassSource resource = Roaster.parse(JavaClassSource.class, output);
		javaFacet.saveJavaSource(resource);
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