data "aws_ami" "ubuntu2" {
  most_recent = true

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-bionic-18.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["099720109477"] # Canonical
}

resource "aws_instance" "web" {
  ami           = data.aws_ami.ubuntu2.id
  instance_type = "t2.micro"
  key_name = "awskey"
  tags = {
    Name = "jenkins-vm"
    team = "presales"
    product = "weather"
    environment = "demo"
    owner = "rohitg"
    bu = "architects"
    function = "cicd"
    costcenter = "007"
  }
}