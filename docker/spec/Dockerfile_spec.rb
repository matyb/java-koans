#! /usr/bin/ruby

require "serverspec"
require "docker"

describe "Dockerfile" do
  before(:all) do
    image = Docker::Image.build_from_dir('.')

    set :os, family: :redhat
    set :backend, :docker
    set :docker_image, image.id
  end

  it "has jdk8 installed" do
    expect(command("javac -version").stderr).to include(" 1.8.")
  end

  it "has gradle installed" do
    expect(command("gradle -version").stdout).to include("Build time")
  end

  it "has maven installed" do
    expect(command("mvn --version").stdout).to include("Apache Maven")
  end

  it "can build app" do
    output = command("git clone https://github.com/DavidWhitlock/java-koans && mvn clean install").stdout
    expect(output).to include("BUILD SUCCESSFUL")
  end

end

