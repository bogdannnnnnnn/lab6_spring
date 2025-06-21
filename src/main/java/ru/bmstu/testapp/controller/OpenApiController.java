package ru.bmstu.testapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiController {

    @GetMapping(value = "/v3/api-docs", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getOpenApiSpec() {
        Map<String, Object> spec = new HashMap<>();
        
        // Basic OpenAPI info
        spec.put("openapi", "3.0.1");
        
        Map<String, Object> info = new HashMap<>();
        info.put("title", "Student Management API");
        info.put("description", "API for managing students and their tokens");
        info.put("version", "1.0");
        spec.put("info", info);
        
        // Servers
        Map<String, String> server = new HashMap<>();
        server.put("url", "/");
        server.put("description", "Default server");
        spec.put("servers", List.of(server));
        
        // Paths
        Map<String, Object> paths = new HashMap<>();
        
        // GET /api/v1/getStatus
        Map<String, Object> getStatus = new HashMap<>();
        Map<String, Object> getStatusOp = new HashMap<>();
        getStatusOp.put("tags", List.of("Health"));
        getStatusOp.put("summary", "Check application status");
        getStatusOp.put("operationId", "getStatus");
        
        Map<String, Object> getStatusResponses = new HashMap<>();
        Map<String, Object> status200 = new HashMap<>();
        status200.put("description", "Application is running");
        
        Map<String, Object> statusContent = new HashMap<>();
        Map<String, Object> statusJson = new HashMap<>();
        Map<String, Object> statusSchema = new HashMap<>();
        statusSchema.put("type", "object");
        
        Map<String, Object> statusProps = new HashMap<>();
        Map<String, Object> statusProp = new HashMap<>();
        statusProp.put("type", "string");
        statusProp.put("example", "OK");
        statusProps.put("status", statusProp);
        
        Map<String, Object> messageProp = new HashMap<>();
        messageProp.put("type", "string");
        messageProp.put("example", "Application is running");
        statusProps.put("message", messageProp);
        
        statusSchema.put("properties", statusProps);
        statusJson.put("schema", statusSchema);
        statusContent.put("application/json", statusJson);
        status200.put("content", statusContent);
        getStatusResponses.put("200", status200);
        
        getStatusOp.put("responses", getStatusResponses);
        getStatus.put("get", getStatusOp);
        paths.put("/api/v1/getStatus", getStatus);
        
        // GET /api/v1/students
        Map<String, Object> getStudents = new HashMap<>();
        Map<String, Object> getStudentsOp = new HashMap<>();
        getStudentsOp.put("tags", List.of("Students"));
        getStudentsOp.put("summary", "Get all students");
        getStudentsOp.put("operationId", "getAllStudents");
        
        // Role parameter
        Map<String, Object> roleParam = new HashMap<>();
        roleParam.put("name", "role");
        roleParam.put("in", "query");
        roleParam.put("description", "User role (student or teacher)");
        roleParam.put("required", false);
        
        Map<String, Object> roleSchema = new HashMap<>();
        roleSchema.put("type", "string");
        roleSchema.put("enum", List.of("student", "teacher"));
        roleParam.put("schema", roleSchema);
        
        getStudentsOp.put("parameters", List.of(roleParam));
        
        Map<String, Object> getStudentsResponses = new HashMap<>();
        Map<String, Object> students200 = new HashMap<>();
        students200.put("description", "List of students");
        
        Map<String, Object> studentsContent = new HashMap<>();
        Map<String, Object> studentsJson = new HashMap<>();
        Map<String, Object> studentsArraySchema = new HashMap<>();
        studentsArraySchema.put("type", "array");
        
        Map<String, Object> studentRef = new HashMap<>();
        studentRef.put("$ref", "#/components/schemas/Student");
        studentsArraySchema.put("items", studentRef);
        
        studentsJson.put("schema", studentsArraySchema);
        studentsContent.put("application/json", studentsJson);
        students200.put("content", studentsContent);
        getStudentsResponses.put("200", students200);
        
        Map<String, Object> students403 = new HashMap<>();
        students403.put("description", "Access forbidden");
        getStudentsResponses.put("403", students403);
        
        getStudentsOp.put("responses", getStudentsResponses);
        
        // POST /api/v1/students
        Map<String, Object> postStudentsOp = new HashMap<>();
        postStudentsOp.put("tags", List.of("Students"));
        postStudentsOp.put("summary", "Add new student");
        postStudentsOp.put("description", "Creates a new student (teacher role required)");
        postStudentsOp.put("operationId", "addStudent");
        
        Map<String, Object> teacherRoleParam = new HashMap<>();
        teacherRoleParam.put("name", "role");
        teacherRoleParam.put("in", "query");
        teacherRoleParam.put("description", "User role (must be 'teacher')");
        teacherRoleParam.put("required", true);
        
        Map<String, Object> teacherRoleSchema = new HashMap<>();
        teacherRoleSchema.put("type", "string");
        teacherRoleSchema.put("enum", List.of("teacher"));
        teacherRoleParam.put("schema", teacherRoleSchema);
        
        postStudentsOp.put("parameters", List.of(teacherRoleParam));
        
        // Request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("required", true);
        
        Map<String, Object> requestContent = new HashMap<>();
        Map<String, Object> requestJson = new HashMap<>();
        Map<String, Object> studentRequestRef = new HashMap<>();
        studentRequestRef.put("$ref", "#/components/schemas/StudentRequest");
        requestJson.put("schema", studentRequestRef);
        requestContent.put("application/json", requestJson);
        requestBody.put("content", requestContent);
        
        postStudentsOp.put("requestBody", requestBody);
        
        Map<String, Object> postResponses = new HashMap<>();
        Map<String, Object> post200 = new HashMap<>();
        post200.put("description", "Student added successfully");
        
        Map<String, Object> post200Content = new HashMap<>();
        Map<String, Object> post200Json = new HashMap<>();
        Map<String, Object> post200Schema = new HashMap<>();
        post200Schema.put("type", "object");
        
        Map<String, Object> post200Props = new HashMap<>();
        Map<String, Object> messageProp200 = new HashMap<>();
        messageProp200.put("type", "string");
        messageProp200.put("example", "Student added successfully");
        post200Props.put("message", messageProp200);
        
        post200Schema.put("properties", post200Props);
        post200Json.put("schema", post200Schema);
        post200Content.put("application/json", post200Json);
        post200.put("content", post200Content);
        postResponses.put("200", post200);
        
        Map<String, Object> post400 = new HashMap<>();
        post400.put("description", "Bad request");
        postResponses.put("400", post400);
        
        Map<String, Object> post403 = new HashMap<>();
        post403.put("description", "Access forbidden - teacher role required");
        postResponses.put("403", post403);
        
        postStudentsOp.put("responses", postResponses);
        
        getStudents.put("get", getStudentsOp);
        getStudents.put("post", postStudentsOp);
        paths.put("/api/v1/students", getStudents);
        
        // GET /api/v1/students/{lastName}/{firstName}
        Map<String, Object> getStudentById = new HashMap<>();
        Map<String, Object> getStudentByIdOp = new HashMap<>();
        getStudentByIdOp.put("tags", List.of("Students"));
        getStudentByIdOp.put("summary", "Get student by name");
        getStudentByIdOp.put("operationId", "getStudent");
        
        // Path parameters
        Map<String, Object> lastNameParam = new HashMap<>();
        lastNameParam.put("name", "lastName");
        lastNameParam.put("in", "path");
        lastNameParam.put("required", true);
        Map<String, Object> lastNameSchema = new HashMap<>();
        lastNameSchema.put("type", "string");
        lastNameParam.put("schema", lastNameSchema);
        
        Map<String, Object> firstNameParam = new HashMap<>();
        firstNameParam.put("name", "firstName");
        firstNameParam.put("in", "path");
        firstNameParam.put("required", true);
        Map<String, Object> firstNameSchema = new HashMap<>();
        firstNameSchema.put("type", "string");
        firstNameParam.put("schema", firstNameSchema);
        
        Map<String, Object> roleParamGet = new HashMap<>();
        roleParamGet.put("name", "role");
        roleParamGet.put("in", "query");
        roleParamGet.put("description", "User role");
        roleParamGet.put("required", false);
        Map<String, Object> roleSchemaGet = new HashMap<>();
        roleSchemaGet.put("type", "string");
        roleSchemaGet.put("enum", List.of("student", "teacher"));
        roleParamGet.put("schema", roleSchemaGet);
        
        getStudentByIdOp.put("parameters", List.of(lastNameParam, firstNameParam, roleParamGet));
        
        Map<String, Object> getByIdResponses = new HashMap<>();
        Map<String, Object> getById200 = new HashMap<>();
        getById200.put("description", "Student found");
        Map<String, Object> getByIdContent = new HashMap<>();
        Map<String, Object> getByIdJson = new HashMap<>();
        Map<String, Object> getByIdRef = new HashMap<>();
        getByIdRef.put("$ref", "#/components/schemas/Student");
        getByIdJson.put("schema", getByIdRef);
        getByIdContent.put("application/json", getByIdJson);
        getById200.put("content", getByIdContent);
        getByIdResponses.put("200", getById200);
        
        Map<String, Object> getById404 = new HashMap<>();
        getById404.put("description", "Student not found");
        getByIdResponses.put("404", getById404);
        
        Map<String, Object> getById403 = new HashMap<>();
        getById403.put("description", "Access forbidden");
        getByIdResponses.put("403", getById403);
        
        getStudentByIdOp.put("responses", getByIdResponses);
        getStudentById.put("get", getStudentByIdOp);
        
        // DELETE /api/v1/students/{lastName}/{firstName}
        Map<String, Object> deleteStudentOp = new HashMap<>();
        deleteStudentOp.put("tags", List.of("Students"));
        deleteStudentOp.put("summary", "Remove student");
        deleteStudentOp.put("description", "Deletes a student (teacher role required)");
        deleteStudentOp.put("operationId", "removeStudent");
        
        Map<String, Object> lastNameParamDel = new HashMap<>();
        lastNameParamDel.put("name", "lastName");
        lastNameParamDel.put("in", "path");
        lastNameParamDel.put("required", true);
        Map<String, Object> lastNameSchemaDel = new HashMap<>();
        lastNameSchemaDel.put("type", "string");
        lastNameParamDel.put("schema", lastNameSchemaDel);
        
        Map<String, Object> firstNameParamDel = new HashMap<>();
        firstNameParamDel.put("name", "firstName");
        firstNameParamDel.put("in", "path");
        firstNameParamDel.put("required", true);
        Map<String, Object> firstNameSchemaDel = new HashMap<>();
        firstNameSchemaDel.put("type", "string");
        firstNameParamDel.put("schema", firstNameSchemaDel);
        
        Map<String, Object> roleParamDel = new HashMap<>();
        roleParamDel.put("name", "role");
        roleParamDel.put("in", "query");
        roleParamDel.put("description", "User role (must be 'teacher')");
        roleParamDel.put("required", true);
        Map<String, Object> roleSchemaDel = new HashMap<>();
        roleSchemaDel.put("type", "string");
        roleSchemaDel.put("enum", List.of("teacher"));
        roleParamDel.put("schema", roleSchemaDel);
        
        deleteStudentOp.put("parameters", List.of(lastNameParamDel, firstNameParamDel, roleParamDel));
        
        Map<String, Object> deleteResponses = new HashMap<>();
        Map<String, Object> delete200 = new HashMap<>();
        delete200.put("description", "Student removed successfully");
        Map<String, Object> delete200Content = new HashMap<>();
        Map<String, Object> delete200Json = new HashMap<>();
        Map<String, Object> delete200Schema = new HashMap<>();
        delete200Schema.put("type", "object");
        Map<String, Object> delete200Props = new HashMap<>();
        Map<String, Object> deleteMessage = new HashMap<>();
        deleteMessage.put("type", "string");
        deleteMessage.put("example", "Student removed successfully");
        delete200Props.put("message", deleteMessage);
        delete200Schema.put("properties", delete200Props);
        delete200Json.put("schema", delete200Schema);
        delete200Content.put("application/json", delete200Json);
        delete200.put("content", delete200Content);
        deleteResponses.put("200", delete200);
        
        Map<String, Object> delete400 = new HashMap<>();
        delete400.put("description", "Student not found");
        deleteResponses.put("400", delete400);
        
        Map<String, Object> delete403 = new HashMap<>();
        delete403.put("description", "Access forbidden - teacher role required");
        deleteResponses.put("403", delete403);
        
        deleteStudentOp.put("responses", deleteResponses);
        getStudentById.put("delete", deleteStudentOp);
        
        paths.put("/api/v1/students/{lastName}/{firstName}", getStudentById);
        
        // PUT /api/v1/students/{lastName}/{firstName}/tokens
        Map<String, Object> putTokens = new HashMap<>();
        Map<String, Object> putTokensOp = new HashMap<>();
        putTokensOp.put("tags", List.of("Students"));
        putTokensOp.put("summary", "Change student tokens");
        putTokensOp.put("description", "Updates student's token balance (teacher role required)");
        putTokensOp.put("operationId", "changeTokens");
        
        Map<String, Object> lastNameParamPut = new HashMap<>();
        lastNameParamPut.put("name", "lastName");
        lastNameParamPut.put("in", "path");
        lastNameParamPut.put("required", true);
        Map<String, Object> lastNameSchemaPut = new HashMap<>();
        lastNameSchemaPut.put("type", "string");
        lastNameParamPut.put("schema", lastNameSchemaPut);
        
        Map<String, Object> firstNameParamPut = new HashMap<>();
        firstNameParamPut.put("name", "firstName");
        firstNameParamPut.put("in", "path");
        firstNameParamPut.put("required", true);
        Map<String, Object> firstNameSchemaPut = new HashMap<>();
        firstNameSchemaPut.put("type", "string");
        firstNameParamPut.put("schema", firstNameSchemaPut);
        
        Map<String, Object> roleParamPut = new HashMap<>();
        roleParamPut.put("name", "role");
        roleParamPut.put("in", "query");
        roleParamPut.put("description", "User role (must be 'teacher')");
        roleParamPut.put("required", true);
        Map<String, Object> roleSchemaPut = new HashMap<>();
        roleSchemaPut.put("type", "string");
        roleSchemaPut.put("enum", List.of("teacher"));
        roleParamPut.put("schema", roleSchemaPut);
        
        putTokensOp.put("parameters", List.of(lastNameParamPut, firstNameParamPut, roleParamPut));
        
        Map<String, Object> putRequestBody = new HashMap<>();
        putRequestBody.put("required", true);
        Map<String, Object> putRequestContent = new HashMap<>();
        Map<String, Object> putRequestJson = new HashMap<>();
        Map<String, Object> tokenChangeRef = new HashMap<>();
        tokenChangeRef.put("$ref", "#/components/schemas/TokenChangeRequest");
        putRequestJson.put("schema", tokenChangeRef);
        putRequestContent.put("application/json", putRequestJson);
        putRequestBody.put("content", putRequestContent);
        putTokensOp.put("requestBody", putRequestBody);
        
        Map<String, Object> putResponses = new HashMap<>();
        Map<String, Object> put200 = new HashMap<>();
        put200.put("description", "Tokens updated successfully");
        Map<String, Object> put200Content = new HashMap<>();
        Map<String, Object> put200Json = new HashMap<>();
        Map<String, Object> put200Schema = new HashMap<>();
        put200Schema.put("type", "object");
        Map<String, Object> put200Props = new HashMap<>();
        Map<String, Object> putMessage = new HashMap<>();
        putMessage.put("type", "string");
        putMessage.put("example", "Tokens updated successfully");
        put200Props.put("message", putMessage);
        put200Schema.put("properties", put200Props);
        put200Json.put("schema", put200Schema);
        put200Content.put("application/json", put200Json);
        put200.put("content", put200Content);
        putResponses.put("200", put200);
        
        Map<String, Object> put400 = new HashMap<>();
        put400.put("description", "Bad request");
        putResponses.put("400", put400);
        
        Map<String, Object> put403 = new HashMap<>();
        put403.put("description", "Access forbidden - teacher role required");
        putResponses.put("403", put403);
        
        putTokensOp.put("responses", putResponses);
        putTokens.put("put", putTokensOp);
        
        paths.put("/api/v1/students/{lastName}/{firstName}/tokens", putTokens);
        
        // Components schemas
        Map<String, Object> components = new HashMap<>();
        Map<String, Object> schemas = new HashMap<>();
        
        // Student schema
        Map<String, Object> studentSchema = new HashMap<>();
        studentSchema.put("type", "object");
        
        Map<String, Object> studentProps = new HashMap<>();
        
        Map<String, Object> lastName = new HashMap<>();
        lastName.put("type", "string");
        lastName.put("example", "Ivanov");
        studentProps.put("lastName", lastName);
        
        Map<String, Object> firstName = new HashMap<>();
        firstName.put("type", "string");
        firstName.put("example", "Ivan");
        studentProps.put("firstName", firstName);
        
        Map<String, Object> tokens = new HashMap<>();
        tokens.put("type", "integer");
        tokens.put("example", 10);
        studentProps.put("tokens", tokens);
        
        studentSchema.put("properties", studentProps);
        schemas.put("Student", studentSchema);
        
        // StudentRequest schema
        Map<String, Object> studentRequestSchema = new HashMap<>();
        studentRequestSchema.put("type", "object");
        studentRequestSchema.put("required", List.of("lastName", "firstName"));
        
        Map<String, Object> requestProps = new HashMap<>();
        
        Map<String, Object> reqLastName = new HashMap<>();
        reqLastName.put("type", "string");
        reqLastName.put("example", "Ivanov");
        requestProps.put("lastName", reqLastName);
        
        Map<String, Object> reqFirstName = new HashMap<>();
        reqFirstName.put("type", "string");
        reqFirstName.put("example", "Ivan");
        requestProps.put("firstName", reqFirstName);
        
        studentRequestSchema.put("properties", requestProps);
        schemas.put("StudentRequest", studentRequestSchema);
        
        // TokenChangeRequest schema
        Map<String, Object> tokenChangeSchema = new HashMap<>();
        tokenChangeSchema.put("type", "object");
        tokenChangeSchema.put("required", List.of("delta"));
        
        Map<String, Object> tokenProps = new HashMap<>();
        
        Map<String, Object> delta = new HashMap<>();
        delta.put("type", "integer");
        delta.put("description", "Token change amount (positive to add, negative to subtract)");
        delta.put("example", 5);
        tokenProps.put("delta", delta);
        
        tokenChangeSchema.put("properties", tokenProps);
        schemas.put("TokenChangeRequest", tokenChangeSchema);
        
        components.put("schemas", schemas);
        spec.put("components", components);
        spec.put("paths", paths);
        
        return spec;
    }
} 