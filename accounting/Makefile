help: ## List available commands
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

build: ## Create a packaged app (target/**.jar)
	./mvnw clean package -DskipTests -Dpmd.skip=true

start: ## Launch a Postgres database and the Spring Boot application
	docker-compose -f ./docker/postgres.yml up -d;
	./mvnw;

test: ## Launch Unit and Integration tests
	./mvnw test

DATE_WITH_TIME := $(shell /bin/date "+%Y%m%d%H%M%S")
migrate-generate: ## Generate database migration files
	./mvnw compile liquibase:diff -Dliquibase.diffChangeLogFile=src/main/resources/db/changelog/changes/${DATE_WITH_TIME}.yaml
	@echo "  - include:" >> src/main/resources/db/changelog/db.changelog-master.yaml
	@echo "      file: db/changelog/changes/$(DATE_WITH_TIME).yaml" >> src/main/resources/db/changelog/db.changelog-master.yaml
	@echo ""
	@echo "MIGRATION FILE GENERATED ($(DATE_WITH_TIME).yaml)."
	@echo "CHECK IF IT SUITS YOUR NEEDS AND ADD ROLLBACK COMMANDS."
	@echo "RESTART THE APP TO APPLY CHANGES."
