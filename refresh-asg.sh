#!/bin/bash
name=$(aws autoscaling describe-auto-scaling-groups --region ap-southeast-2 | jq '.AutoScalingGroups[0].AutoScalingGroupName')
asg_name=`echo $name | cut -d '"' -f2`
aws autoscaling set-desired-capacity --auto-scaling-group-name $asg_name --desired-capacity 0 --region ap-southeast-2
sleep 60
aws autoscaling set-desired-capacity --auto-scaling-group-name $asg_name --desired-capacity 1 --region ap-southeast-2
