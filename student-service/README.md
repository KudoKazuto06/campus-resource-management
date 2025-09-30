# 🎓 Student Service - Campus Resource Management

This microservice manages student profiles within the **Campus Resource Management System**.  
It provides APIs to create, update, filter, and view student details.

---

## 🛠️ Tech Stack

| 🧩 Component            | 🔎 Description                            |
|-------------------------|-------------------------------------------|
| ☕ Java 17               | Programming language                      |
| 🌱 Spring Boot          | Backend framework                         |
| 📂 Spring Data JPA      | ORM and database access                   |
| 🐘 PostgreSQL           | Relational database                       |
| 📡 Kafka                | Event streaming for inter-service         |
| 🤝 Feign Client         | HTTP client for microservices             |
| 🚌 Spring Cloud Bus     | Configuration propagation & events        |
| 🔐 Keycloak             | Identity and Access Management Provider   |
| 🌐 Spring Web           | Building REST APIs                        |
| ⚙️ Spring Cloud Config  | Centralized configuration management      |
| 🛡 Spring Security      | Secure API endpoints and user sessions    |
| 🔁 MapStruct            | DTO mapping                               |
| 🧱 Liquibase            | Database version control                  |
| 🧪 JUnit + Mockito      | Unit and integration testing              |
| 🐳 Docker + Compose     | Containerization and orchestration        |
| 🧠 Redis                | In-memory data store (e.g. token/session) |
| 📘 Swagger / OpenAPI    | API documentation                         |
| ✨ Lombok                | Reduce Java boilerplate                   |
| 📦 Maven                | Build and dependency management           |
---

## 📁 Project Structure

````
├── src/main/java
│   └── com/campus-resource-management/student-service
│       ├── controller          # 🎯 REST API controllers
│       ├── service             # 🧠 Business logic layer (CRUD)
│       ├── constant            # 🧾 Constant values (message, status codes, enum, etc.)
│       ├── entity              # 🧬 JPA entities
│       ├── repository          # 💾 Spring Data JPA
│       ├── dto                 # 📦 Request/response data transfer
│       ├── exception           # 🚨 Custom exceptions
│       ├── mapper              # 🔁 Entity <-> DTO mappers
│       ├── validation          # ✔️ Custom validators
│       ├── config              # ⚙️ Security & global configs
│       ├── security            # 🔒 Security handling (resource server config, JWT)
│       └── PatientServiceApplication.java
├── src/test/java
│   └── com/campus-resource-management/student-service
│       ├── controller          # 🧪 Unit Tests for Controllers
│       ├── service             # 🧪 Unit Test for Service Layer
├── src/main/resources
│       ├── application.yml     # 🧾 Main application configuration
│       ├── logs/               # 📝 Application logs
└── README.md                   # 📘 Project documentation

````

---

## ⚙️ How to Run Locally

### ✅ Prerequisites

- [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) (recommended)
- Lombok plugin enabled in IntelliJ
  > `File → Settings → Plugins → Search for Lombok → Install`

  > `Enable annotation processing under Build → Compiler → Annotation Processors`

---

### 📥 1. Clone All Repositories

later... the project still has not yet finished

```bash
mvn clean package
```

### ✅ What Happens When You Run `mvn clean package`

When you run the following command in any service directory:

```bash
mvn clean package
```

---

This will automatically:

- 🧪 **Run all unit tests**.
- 📦 **Package the application** into a `.jar` file if all tests pass.

---

### 🔍 Build Results

| Outcome             | Description                                                                 |
|---------------------|-----------------------------------------------------------------------------|
| ✅ **BUILD SUCCESS** | All unit tests passed. A `.jar` file is generated in the `target/` folder. |
| ❌ **BUILD FAILURE** | One or more unit tests failed. Maven stops the build and provides error details. |

---


### 🧾 Where to Check Test Results

If the build fails, detailed test reports will be available in:

```
target/surefire-reports/
```

You can open the `.txt` files there to inspect what went wrong.

---

### ⚠️ Optional: Skip Tests (Not Recommended)

If you want to skip tests temporarily (e.g., for demo purposes), use:

```bash
mvn clean package -DskipTests
```

> ⚠️ Use this with caution — skipping tests may cause runtime errors if something is broken.

---

### 🛠 Troubleshooting Tips

If tests fail:

- 🔍 Check for missing test data, mock misconfigurations, or DTO/validation issues.
- 🔐 Ensure Keycloak or external services are running if your test depends on them.
- 🔁 Fix the error and re-run `mvn clean package`.

---

### 🐘 2. Run PostgreSQL via Docker

🐋 **Add docker-compose.yml to your project**,
e.g ```~\User\campus-resource-management\docker-compose.yml```

📄 Use the sample docker-compose content at ```/src_readme/docker-compose.yml```

Your final should look something like this:

````
├── <Project_Name>
│   ├── api-gateway             # 🧾 API-GATEWAY
│   ├── studentprofile-service  # 🎓 STUDENT-PROFILE SERVICE
│   ├──
````

📦 **Start and View Docker containers:**
```bash
docker-compose up -d --build    # Start Docker Containers
docker ps                       # View Docker Containers
```

### 🧱 3. Run the App (Currently still have not integrated - Do not have to test now)

#### 🔐 Step 1: Access Keycloak Admin Console

**Go to** [http://localhost:8082](http://localhost:8082) and **enter the Credentials:**

---

#### 🏗️ Step 2: Import Realm Configuration

Create a new Realm using the `realm-export.json` in `/src_readme`:
1. Go to **Realm Settings → Import**
2. Upload the file and click **Create**

---

#### 🛠 Step 3: Configure User Profile

Modify the User Profile Config using the `userprofile-config.json` in `/src_readme`:
1. Go to **Realm Settings → User Profile**
2. Paste or upload the content 

---

#### 👤 Step 4: Create a New User

Create a new user to use or test:
1. Navigate to **Users → Add User**
2. Fill in the required fields and click **Save** 

---

#### 🔑 Step 5: Set Password and Assign Role
Update passwords/credentials and assign role for your account:
1. Go to the user’s **Credentials** tab → Set password
2. Go to **Role Mappings** tab → Assign roles  

---

#### 🔓 Step 6: Login and Retrieve Access Token

Login and obtain the access token:  
You can use Postman, Insomnia, or a frontend app to log in using the created user and get the token.
> This token is required for authorized API access.

---

### 📡 4. API Endpoints

**Base URL:** ```http://localhost:8080/student-profile```  
**Gateway URL:** ```http://localhost:8090/student-profile```  (not yet implemented)

**Error Code for Student-Profile Service:** ```400XXX```

---

### ➕ Create Student Profile
**POST** `/create`

- Request Body: `CreateStudentProfileRequest`
- Response: `ServiceResponse<StudentProfileDetailResponse>`

#### ✅ Valid Request Screenshot
#### ❌ Invalid Request Screenshot

---

### ✏️ Update Student Profile
**PUT** `/update/{studentId}`

- Path Variable: `studentId`
- Request Body: `UpdateStudentProfileRequest`
- Response: `ServiceResponse<StudentProfileDetailResponse>`

#### ✅ Valid Request Screenshot
#### ❌ Invalid Request Screenshot

---

### 🔍 Get Student Profile by ID
**GET** `/detail/{studentId}`

- Path Variable: `studentId`
- Response: `ServiceResponse<StudentProfileDetailResponse>`

#### ✅ Valid Request Screenshot
#### ❌ Invalid Request Screenshot

---

### 📄 Filter / Search / Sort Student Profiles
**POST** `/filter`

- Request Body: `FilterStudentProfileRequest`
- Query Params:
    - `page` (default 0)
    - `size` (default 10)
- Response: `ServiceResponse<PaginationResponse<FilterStudentProfileResponse>>`

#### ✅ Valid Request Screenshot
#### ❌ Invalid Request Screenshot

---

### 🗑 Soft Delete Student Profile
**DELETE** `/delete/{studentId}`

- Path Variable: `studentId`
- Response: `ServiceResponse<Void>`

#### ✅ Valid Request Screenshot
#### ❌ Invalid Request Screenshot

---

### ♻️ Restore Student Profile
**PATCH** `/restore/{studentId}`

- Path Variable: `studentId`
- Response: `ServiceResponse<StudentProfileDetailResponse>`

#### ✅ Valid Request Screenshot
#### ❌ Invalid Request Screenshot

---

---

> 🧾 All APIs return a standard `ServiceResponse` containing:
> - `statusCode`: see `StatusCode.java`
> - `message`: see `MessageResponse.java`
> - `data`: object or list depending on endpoint
>
> 🚨 **General Student-Controller Service Response Codes:**
> - `204` - No Content
> - `400` - Bad Request / Error
> - `401` - Unauthorized
> - `403` - Forbidden
> - `404` - Not Found
> - `409` - Existed
> - `500` - Internal Server Error

---

## 🗂️ How to View the Database

You can inspect and browse the PostgreSQL database using **Docker CLI** or a **GUI like IntelliJ**.

### 🐳 Option 1: View with Docker CLI

#### 1. Open terminal and run:
```bash
docker ps
```

#### 2. Copy the container ID of your PostgreSQL instance.

#### 3. Run:
```bash
docker exec -it <container_id> psql -U postgres
```

#### 4. Connect to the correct database:
```bash
\c student_db
```

#### From there you can run SQL commands like:
```bash
SELECT * FROM students;
```

### 🚀 Alternative: Access Postgres Database in Docker via PGAdmin4

### 🧠 Option 2: Using IntelliJ IDEA (Database Tool)

#### You can connect IntelliJ directly to your PostgreSQL container.
> - JDBC URL: your_database
> - Username: your_username
> - Password: your_password

---

## 🔌 How to Use the API

Once the application is running, you can interact with the API using:

### 🧭 Swagger UI (Interactive Documentation)
- Open in your browser:

```bash
http://localhost:8080/student-profile
```

#### Features:
- Explore all API endpoints
- See request and response schemas
- Use the **"Try it out"** button to test APIs directly
- View available query parameters and response codes

---

## 🤝 Contribution Guidelines

#### To contribute to this project, follow the steps below:

1. **Fork** the repository

2. **Clone** your forked repo:
```bash
git clone -b branch_name https://github.com/KudoKazuto06/campus-resource-management
cd student-service
```   

3. **Create** a new branch:
```bash
git checkout -b <feature/your-feature-name> 
```

4. **Make your changes**, then stage and commit:
```bash
git add .
git commit -m "Add: meaningful message describing your change"
```

5. **Push your branch to origin**:
```bash
git push origin <feature/your-feature-name>
```

6. **Open a Merge Request (MR)** on the Gitlab web interface

→ Source: `feature/your-feature-name`
→ Target: `main` branch

> ⚠️ Please follow the existing code style, formatting rules, and commit message conventions. Use English for all code, variables, and comments.

> ✅ All new features should include appropriate unit tests and Swagger annotations.

---