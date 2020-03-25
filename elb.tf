######
# ELB
######
module "elb" {
  source = "terraform-aws-modules/elb/aws"

  name = "elb-demo"

  subnets         = module.vpc.public_subnets
  security_groups = [data.aws_security_group.default.id]
  internal        = false

  listener = [
    {
      instance_port     = "3000"
      instance_protocol = "HTTP"
      lb_port           = "3000"
      lb_protocol       = "HTTP"
    },
  ]

  health_check = {
    target              = "HTTP:3000/"
    interval            = 30
    healthy_threshold   = 2
    unhealthy_threshold = 2
    timeout             = 5
  }

  tags = {
    Owner       = "user"
    Environment = "dev"
  }
}