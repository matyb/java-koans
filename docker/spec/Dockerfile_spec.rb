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

  it "can build app" do
    output = command("git clone https://github.com/matyb/java-koans && gradle -p java-koans/lib buildApp").stdout
    expect(output).to include(":test\n")
    expect(output).to include("BUILD SUCCESSFUL")
  end

end

