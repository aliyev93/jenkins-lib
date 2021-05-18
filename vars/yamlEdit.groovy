import org.yaml.snakeyaml.*
import org.yaml.snakeyaml.constructor.*
import groovy.transform.*
import org.yaml.snakeyaml.Yaml
import  java.nio.file.Path
import groovy.io.FileType
import groovy.json.JsonSlurper

def call(env, imgname, imgtag){
    def file = readFile(file: "${env}.yaml")
    def yamlfile = new Yaml().load(file)
    yamlfile.image.name="${imgname}"
    yamlfile.image.tag="${imgtag}"
    //yamlfile.secrets.vaults[0]=null
    //yamlfile.secrets.vaults[0].valueFrom.name='secret-Vault-Test'
    //yamlfile.secrets.vaults[0].valueFrom.value='testVaultValue'
    writeYaml file: "${env}.yaml", data: yamlfile, overwrite: true
    chartYaml()
}

def chartYaml(){
    echo "chartYaml"
    def file1 = readFile(file: "Chart.yaml")
    def yamlfile1 = new Yaml().load(file1)
    nv = semVer(yamlfile1.appVersion)
    yamlfile1.appVersion = nv
    writeYaml file: "Chart.yaml", data: yamlfile1, overwrite: true
}

def semVer(ver) {
    def arr = ver.split("\\.")
    def newVer = arr[-1].toInteger() + 1
    //arr.last()==newVer.toString()
    arr[-1]=newVer.toString()

    return arr.join(".")
}