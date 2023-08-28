import paramiko
from paramiko import SSHClient
import logging

hostname = "177.26.254.135"
port = 8057
password = " "
username = 'cissys'
private_key = "C:/Users/anilkodam/IdeaProjects/CCRS/src/test/resources/id_rsa.ppk"
key = paramiko.RSAKey.from_private_key_file(filename=private_key, password=password)
destination_path = '/opt/CCBERP/CCBERP/SFTP/CM-UPRBK/working/SLTA03XXXXXX.CASP_MT940.D20221711122335.txt'
source_path = 'C:/Users/anilkodam/IdeaProjects/CCRS/src/test/resources/datafile/SLTA03XXXXXX.CASP_MT940.D20221711122335.txt'

print("File transfer started....")
with SSHClient() as ssh:
    ssh.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh.connect(hostname, username=username, pkey=key, port=port)
    with ssh.open_sftp() as sftp:
        sftp.put(source_path, destination_path)

print("File transfer completed....")

