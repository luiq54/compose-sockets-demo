import socket
import _thread

HOST = "192.168.0.103"
PORT = 8080


def multi_threaded_client(connection, address):
    print("Connected by %s" % addr[0])
    x = int(connection.recv(1024).decode('utf-8'))
    rad = int(connection.recv(1024).decode('utf-8'))
    y = (x + rad) / 2
    print("Received X: %s Rad:%s, Sending Y:%s" % (x, rad, y))
    data = "Y: %s\n" % (str(y))
    connection.sendall(bytes(data, 'utf-8'))
    connection.close()


with socket.socket() as s:
    s.bind((HOST, PORT))
    s.listen()
    print("Server started")
    while True:
        conn, addr = s.accept()
        _thread.start_new_thread(multi_threaded_client, (conn, addr))
