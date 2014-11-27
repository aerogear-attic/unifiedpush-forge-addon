package org.jboss.aerogear.commands;

import org.jboss.forge.addon.dependencies.Dependency;
import org.jboss.forge.addon.dependencies.builder.DependencyBuilder;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.dependencies.DependencyInstaller;
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
import org.jboss.forge.addon.ui.hints.InputType;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.util.Metadata;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;

import freemarker.template.Template;

import java.io.File;
import java.io.InputStreamReader;
import java.lang.Override;
import java.lang.Exception;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class UnifiedPushSetupCommand extends AbstractProjectCommand {

	@Inject
	private DependencyInstaller dependencyInstaller;
	
	@Inject
	private ResourceFactory resourceFactory;

	
	@Inject
	private ProjectFactory projectFactory;
	
	@Inject 
	private TemplateFactory templateFactory;

	@Inject
	@WithAttributes(label = "Server URL")
	private UIInput<String> serverURL;

	@Inject
	@WithAttributes(label = "PushApplication id")
	private UIInput<String> pushApplicationId;

	@Inject
	@WithAttributes(label = "Master Secret", type = InputType.SECRET)
	private UIInput<String> masterSecret;

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(UnifiedPushSetupCommand.class)
				.name("unifiedpush: Setup")
				.category(Categories.create("unifiedpush"));
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {
		builder.add(serverURL).add(pushApplicationId).add(masterSecret);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		Dependency dependency =
				DependencyBuilder.create("org.jboss.aerogear")
				.setArtifactId("unifiedpush-java-client")
				.setVersion("1.1.0-SNAPSHOT");
		
		dependencyInstaller.install(getSelectedProject(context), dependency);
		
		
		URL templateUrl = getClass().getResource("org/jboss/aerogear/pushConfiguration.ftl");
		Resource<?> resource = resourceFactory.create(templateUrl);
		org.jboss.forge.addon.templates.Template template = templateFactory.create(resource, FreemarkerTemplate.class);
		Map<String,Object> params = new HashMap<String,Object>(); //Could also be a POJO also.
		params.put("serverUrl", serverURL.getValue());
		params.put("pushApplicationId", pushApplicationId.getValue());
		params.put("masterSecret", masterSecret.getValue());
		String output = template.process(params); // should return "Hello JBoss Forge".
		return Results
				.success("Command 'unifiedpush: Setup' successfully executed! " + output);
	}

	@Override
	protected boolean isProjectRequired() {
		return true;
	}

	@Override
	protected ProjectFactory getProjectFactory() {
		return projectFactory;
	}

	
}