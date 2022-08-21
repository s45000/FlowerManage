import socket

def get_bytes_stream(sock, length):
    buf = b''
    try:
        step = length
        while True:
            data = sock.recv(step)
            buf += data
            if len(buf) == length:
                break
            elif len(buf) < length:
                step = length - len(buf)
    except Exception as e:
        print(e)
    return buf[:length]

print(socket.gethostbyname(socket.gethostname()))
host = socket.gethostbyname(socket.gethostname())
port = 8088

server_sock = socket.socket(socket.AF_INET)
server_sock.bind((host,port))
server_sock.listen(10)

idx = 0
while idx <= 20:
    idx += 1
    print('wait...')
    client_sock, addr = server_sock.accept()

    len_bytes_string = bytearray(client_sock.recv(1024))[2:]
    print(type(len_bytes_string))
    print(len_bytes_string)
    len_bytes = len_bytes_string.decode('utf-8')
    print(type(len_bytes))
    print(len_bytes)
    length = int(len_bytes)

    img_bytes = get_bytes_stream(client_sock, length)
    img_path = 'saved_image/img'+str(addr[1])+'_'+str(idx)+'.jpg'

    with open(img_path, 'wb') as writer:
        writer.write(img_bytes)
    print(img_path+' is saved')
    client_sock.close()

