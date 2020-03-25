terraform {
    backend "remote" {
        organization = "int"
        workspaces {
            name = "demo"
        }
    }
}