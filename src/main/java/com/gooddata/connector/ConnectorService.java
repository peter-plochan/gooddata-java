/*
 * Copyright (C) 2007-2014, GoodData(R) Corporation. All rights reserved.
 */
package com.gooddata.connector;

import com.gooddata.AbstractService;
import com.gooddata.FutureResult;
import com.gooddata.GoodDataException;
import com.gooddata.PollHandler;
import com.gooddata.gdc.UriResponse;
import com.gooddata.project.Project;
import com.gooddata.project.ProjectService;
import com.gooddata.project.ProjectTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

import static com.gooddata.Validate.notNull;

/**
 * Connector Service
 */
public class ConnectorService extends AbstractService {

    private final ProjectService projectService;

    public ConnectorService(final RestTemplate restTemplate, final ProjectService projectService) {
        super(restTemplate);
        this.projectService = notNull(projectService, "projectService");
    }

    public Integration createIntegration(final Project project, final Settings settings) {
        notNull(project, "project");
        notNull(settings, "settings");
        final Collection<ProjectTemplate> projectTemplates = projectService.getProjectTemplates(project);
        if (projectTemplates == null || projectTemplates.isEmpty()) {
            throw new GoodDataException("Project " + project.getId() + " doesn't contain a template reference");
        }
        final ProjectTemplate template = notNull(projectTemplates.iterator().next(), "project template");
        final Integration integration = createIntegration(project, settings.getConnector(), new Integration(template.getUrl()));
        updateSettings(project, settings);
        return integration;
    }

    public Integration createIntegration(final Project project, final Connector connector, final Integration integration) {
        notNull(project, "project");
        notNull(connector, "connector");
        notNull(integration, "integration");
        return restTemplate.postForObject(Integration.URL, integration, Integration.class, project.getId(), connector.getName());
    }

    public <T extends Settings> void updateSettings(final Project project, final T settings) {
        notNull(settings, "settings");
        notNull(project, "project");
        // todo catch - not every user is allowed to change all settings
        restTemplate.put(Settings.URL, settings, project.getId(), settings.getConnector().getName());
    }

    public FutureResult<Process> startProcess(Project project, ProcessExecution execution) {
        notNull(project, "project");
        notNull(execution, "execution");
        final String connectorName = execution.getConnector().getName();
        // todo try catch
        final UriResponse response = restTemplate.postForObject(Process.URL, execution, UriResponse.class, project.getId(), connectorName);
        return new FutureResult<>(this, new PollHandler<>(response.getUri(), Process.class));
    }
}
