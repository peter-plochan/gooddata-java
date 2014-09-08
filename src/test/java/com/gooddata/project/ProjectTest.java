package com.gooddata.project;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertThat;

public class ProjectTest {

    @Test
    public void testDeserialize() throws Exception {
        final Project project = new ObjectMapper()
                .readValue(getClass().getResourceAsStream("/project/project.json"), Project.class);
        assertThat(project, is(notNullValue()));

        assertThat(project.getAuthorizationToken(), is("AUTH_TOKEN"));
        assertThat(project.getDriver(), is("Pg"));
        assertThat(project.getGuidedNavigation(), is("1"));
        assertThat(project.getCluster(), is("CLUSTER"));
        assertThat(project.isPublic(), is(false));
        assertThat(project.getState(), is("ENABLED"));

        assertThat(project.getTitle(), is("TITLE"));
        assertThat(project.getSummary(), is("DESC"));
        assertThat(project.getAuthor(), is("/gdc/account/profile/USER_ID"));
        assertThat(project.getContributor(), is("/gdc/account/profile/CONTRIB_USER_ID"));
        assertThat(project.getCreated(), is("2014-04-11 11:43:45"));
        assertThat(project.getUpdated(), is("2014-04-11 11:43:47"));

        assertThat(project.getLdmThumbnailLink(), is("/gdc/projects/PROJECT_ID/ldm?thumbnail=1"));
        assertThat(project.getUri(), is("/gdc/projects/PROJECT_ID"));
        assertThat(project.getClearCachesLink(), is("/gdc/projects/PROJECT_ID/clearCaches"));
        assertThat(project.getInvitationsLink(), is("/gdc/projects/PROJECT_ID/invitations"));
        assertThat(project.getUsersLink(), is("/gdc/projects/PROJECT_ID/users?link=1"));
        assertThat(project.getGroupsLink(), is("/gdc/projects/PROJECT_ID/groups"));
        assertThat(project.getUploadsLink(), is("https://ea-di.staging.getgooddata.com/project-uploads/PROJECT_ID/"));
        assertThat(project.getLdmLink(), is("/gdc/projects/PROJECT_ID/ldm"));
        assertThat(project.getMetadataLink(), is("/gdc/md/PROJECT_ID"));
        assertThat(project.getPublicArtifactsLink(), is("/gdc/projects/PROJECT_ID/publicartifacts"));
        assertThat(project.getRolesLink(), is("/gdc/projects/PROJECT_ID/roles"));
        assertThat(project.getDataLoadLink(), is("/gdc/projects/PROJECT_ID/dataload"));
        assertThat(project.getConnectorsLink(), is("/gdc/projects/PROJECT_ID/connectors"));
        assertThat(project.getExecuteLink(), is("/gdc/projects/PROJECT_ID/execute"));
        assertThat(project.getSchedulesLink(), is("/gdc/projects/PROJECT_ID/schedules"));
        assertThat(project.getTemplatesLink(), is("/gdc/md/PROJECT_ID/templates"));
        assertThat(project.getEventStoresLink(), is("/gdc/projects/PROJECT_ID/dataload/eventstore/stores"));
    }

    @Test
    public void testSerialize() throws Exception {
        final Project project = new Project("TITLE", "SUMMARY", "TOKEN");
        project.setProjectTemplate("/projectTemplates/TEMPLATE");
        final String serializedProject = new ObjectMapper().writeValueAsString(project);

        assertThat(serializedProject, startsWith("{\"project\""));

        assertThat(serializedProject, containsString("\"content\""));
        assertThat(serializedProject, containsString("\"authorizationToken\":\"TOKEN\""));
        assertThat(serializedProject, containsString("\"driver\":\"Pg\""));
        assertThat(serializedProject, containsString("\"guidedNavigation\":\"1\""));
        assertThat(serializedProject, not(containsString("\"cluster\"")));
        assertThat(serializedProject, not(containsString("\"isPublic\"")));
        assertThat(serializedProject, not(containsString("\"state\"")));

        assertThat(serializedProject, containsString("\"meta\""));
        assertThat(serializedProject, containsString("\"title\":\"TITLE\""));
        assertThat(serializedProject, containsString("\"summary\":\"SUMMARY\""));
        assertThat(serializedProject, containsString("\"projectTemplate\":\"/projectTemplates/TEMPLATE\""));
        assertThat(serializedProject, not(containsString("\"author\"")));
        assertThat(serializedProject, not(containsString("\"contributor\"")));
        assertThat(serializedProject, not(containsString("\"created\"")));
        assertThat(serializedProject, not(containsString("\"updated\"")));

        assertThat(serializedProject, not(containsString("\"links\"")));
    }
}