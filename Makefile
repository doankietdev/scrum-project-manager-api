# === Detect OS for M2_REPO setup ===
OS := $(shell uname -s)

ifeq ($(OS),Linux)
	M2_REPO := $(HOME)/.m2
else ifeq ($(OS),Darwin)
	M2_REPO := $(HOME)/.m2
else ifeq ($(OS),MINGW64_NT)
	M2_REPO := /c/Users/$(USERNAME)/.m2
else
	$(error Unsupported OS: $(OS))
endif

export M2_REPO

# === Variables ===
DOCKER_COMPOSE_DEV_FILE = docker-compose-dev.yml

# === Phony targets ===
.PHONY: dev dev-stop dev-restart help

# === Dev lifecycle commands ===
dev: ## Start development services
	docker compose -f $(DOCKER_COMPOSE_DEV_FILE) up -d

dev-stop: ## Stop development services
	docker compose -f $(DOCKER_COMPOSE_DEV_FILE) down

dev-restart: dev-stop dev ## Restart development services

logs-%: ## Show logs for a service, e.g., make logs-api
	docker compose -f $(DOCKER_COMPOSE_DEV_FILE) logs -f $*

ps: ## Show running services and their status
	docker compose -f $(DOCKER_COMPOSE_DEV_FILE) ps

help: ## Show this help message
	@echo "Available targets:"
	@grep -E '^[a-zA-Z_-]+(%|-)?[a-zA-Z_]*:.*?## .*$$' $(MAKEFILE_LIST) | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2}'

