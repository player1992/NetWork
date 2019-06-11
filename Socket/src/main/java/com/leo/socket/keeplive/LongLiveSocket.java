package com.leo.socket.keeplive;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * <p>Date:2019/6/11.3:30 PM</p>
 * <p>Author:niu bao</p>
 * <p>Desc:简单看下Socket</p>
 */
public class LongLiveSocket {
    private static final long RETRY_INTERVAL_MILLIS = 3 * 1000;
    private static final long HEART_BEAT_INTERVAL_MILLIS = 5 * 1000;
    private static final long HEART_BEAT_TIMEOUT_MILLIS = 2 * 1000;
    Handler mWriterHandler;
    Handler mUIHandler;

    public interface ErrorCallback {
        boolean onError();
    }

    public interface DataCallback {
        void onData(byte[] data, int offset, int len);
    }

    public interface WritingCallback {
        void onSuccess();

        void onFail(byte[] data, int offset, int len);
    }

    public LongLiveSocket(String host, int port, DataCallback dataCallback, ErrorCallback errorCallback) {
    }

    public void write(byte[] data, WritingCallback callback) {

    }

    public void write(final byte[] data, final int offset, final int len, final WritingCallback callback) {
        mWriterHandler.post(new Runnable() {
            @Override
            public void run() {
                Socket socket = getSocket();
                if (socket == null) {
                    //
                }
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    DataOutputStream out = new DataOutputStream(outputStream);
                    out.writeInt(len);
                    out.write(data,offset,len);
                    callback.onSuccess();
                } catch (Exception e) {
                    callback.onFail(data, offset, len);
                }
            }
        });


    }

    private Socket getSocket() {
        return null;
    }

    public void close() {

    }


    private final Runnable mHeartBeatTask = new Runnable() {

        private byte [] mHeartBeat = new byte[0];

        @Override
        public void run() {
            write(mHeartBeat, new WritingCallback() {
                @Override
                public void onSuccess() {
                    //每隔10秒心跳一次
                    mWriterHandler.postDelayed(mHeartBeatTask,HEART_BEAT_INTERVAL_MILLIS);
                    //如果Socket出现异常就关闭，如果正常就移除CallBack
                    mUIHandler.postDelayed(mHeartBeatTimeoutTask,HEART_BEAT_TIMEOUT_MILLIS);
                }

                @Override
                public void onFail(byte[] data, int offset, int len) {

                }
            });
        }
    };

    private final Runnable mHeartBeatTimeoutTask = new Runnable() {
        @Override
        public void run() {
            Log.e("", "mHeartBeatTimeoutTask#run: heart beat timeout");
//            closeSocket();
        }
    };
}
