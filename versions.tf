
terraform {
  required_version = ">= 0.12"
}

provider "aws" {
  version = ">= 2.11"
  region = var.region
}
