/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.bot.c3p0.database.LineMessageDBFunction;
import com.example.bot.line.Object.LineMessageInfo;
import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.BeaconEvent;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.JoinEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.VideoMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.AudioMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.VideoMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
	String channeltoken = "pslrmKmSM30059ArgBD2wv4Sm6zRbyjqpdrYIHFZbtGZmqO76wuOBV5p2+re039F7umgZptlue+RUiv+k38Oin6v1DIt5wfS8myZ1Xw3h7RPRDczDJgakudp0I8EheQ+VLE77SiMvDtMGUxcg7nvXAdB04t89/1O/w1cDnyilFU=";
	@Autowired
	private LineMessagingClient lineMessagingClient;

	static Path downloadedContentDir;

	public static void main(String[] args) {
		SpringApplication.run(EchoApplication.class, args);
	}
	//add user save userid to db
	@EventMapping
	public void handleDefaultMessageEvent(FollowEvent event) {
		System.out.println("joinevent" + event.getSource().getSenderId());
		LineMessageDBFunction lineMessageDBFunction = new LineMessageDBFunction();
		lineMessageDBFunction.insertLineUserid(event);
	}

	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		// can use
		// TextMessage textMessage = new TextMessage("hello");
		// PushMessage pushMessage = new PushMessage(
		// "Uc7a46420c8125d4fcaa0312f2d47dc10",
		// textMessage
		// );
		// Response<BotApiResponse> response = null;
		// try {
		// response = LineMessagingServiceBuilder
		// .create("pslrmKmSM30059ArgBD2wv4Sm6zRbyjqpdrYIHFZbtGZmqO76wuOBV5p2+re039F7umgZptlue+RUiv+k38Oin6v1DIt5wfS8myZ1Xw3h7RPRDczDJgakudp0I8EheQ+VLE77SiMvDtMGUxcg7nvXAdB04t89/1O/w1cDnyilFU=")
		// .build()
		// .pushMessage(pushMessage)
		// .execute();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println(response.code() + " " + response.message());

		LineMessageDBFunction lineMessageDBFunction = new LineMessageDBFunction();
		LineMessageInfo lineMessageInfo = new LineMessageInfo();
		lineMessageInfo.setChanneltoken(channeltoken);
		lineMessageInfo.setEpochsecond(String.valueOf(event.getTimestamp().getEpochSecond()));
		lineMessageInfo.setMessagetype("text");
		lineMessageInfo.setReplytoken(event.getReplyToken());
		lineMessageInfo.setSenderid(event.getSource().getSenderId());
		lineMessageInfo.setText(event.getMessage().getText());

		lineMessageDBFunction.insertLineMessageInfo(lineMessageInfo);
		return new TextMessage("I am Groot!");

	}

	@EventMapping
	public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
		// You need to install ImageMagick
		System.out.println("ImageMessageContent");
		handleHeavyContent(event.getReplyToken(), event.getMessage().getId(), responseBody -> {
			DownloadedContent jpg = saveContent("jpg", responseBody);
			System.out.println("149"+jpg.getPath());
			System.out.println("150"+jpg.getUri());
			File myFile = new File(jpg.getPath().toString());
			String movefile = APPLocation()+"\\src\\main\\resources\\static\\downloaded\\"+myFile.getName();
			myFile.renameTo(new File(movefile));
			DownloadedContent previewImg = createTempFile("jpg");
			System.out.println("152"+previewImg.getPath());
			System.out.println("153"+previewImg.getUri());
			system("convert", "-resize", "240x", jpg.path.toString(), previewImg.path.toString());
			System.out.println("160"+jpg.getUri());
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
//				 TODO Auto-generated catch block
				e.printStackTrace();
			}
			reply(((MessageEvent) event).getReplyToken(), new ImageMessage(jpg.getUri(), jpg.getUri()));
		});
	}

	private void handleHeavyContent(String replyToken, String messageId,
			Consumer<MessageContentResponse> messageConsumer) {
		final MessageContentResponse response;
		try {
			response = lineMessagingClient.getMessageContent(messageId).get();
		} catch (InterruptedException | ExecutionException e) {
			reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
			throw new RuntimeException(e);
		}
		messageConsumer.accept(response);
	}

	private void reply(@NonNull String replyToken, @NonNull Message message) {
		reply(replyToken, Collections.singletonList(message));
	}

	private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
		try {
			BotApiResponse apiResponse = lineMessagingClient.replyMessage(new ReplyMessage(replyToken, messages)).get();
//			 log.info("Sent messages: {}", apiResponse);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private static DownloadedContent saveContent(String ext, MessageContentResponse responseBody) {
		// log.info("Got content-type: {}", responseBody);
		System.out.println("DownloadedContent");
		DownloadedContent tempFile = createTempFile(ext);
		try (OutputStream outputStream = Files.newOutputStream(tempFile.path)) {
			ByteStreams.copy(responseBody.getStream(), outputStream);
			// log.info("Saved {}: {}", ext, tempFile);
			return tempFile;
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	public static String APPLocation() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		return s;
	}
	private static DownloadedContent createTempFile(String ext) {
		try {
			downloadedContentDir = Files.createTempDirectory("line-bot");

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileName = UUID.randomUUID().toString() + '.' + ext;
		Path tempFile = EchoApplication.downloadedContentDir.resolve(fileName);
		tempFile.toFile().deleteOnExit();
		return new DownloadedContent(tempFile, createUri( "downloaded/"+tempFile.getFileName().toString()));
	}

	private static String createUri(String path) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(path).build().toUriString();
	}

	@Value
	public static class DownloadedContent {
		public DownloadedContent(Path tempFile, String createUri) {
			this.path = tempFile;
			this.uri = createUri;
			// TODO Auto-generated constructor stub
		}

		public String getUri() {
			// TODO Auto-generated method stub
			return this.uri;
		}
		public Path getPath() {
			// TODO Auto-generated method stub
			return this.path;
		}
		Path path;
		String uri;
	}

	private void system(String... args) {
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		try {
			Process start = processBuilder.start();
			int i = start.waitFor();
//			log.info("result: {} =>  {}", Arrays.toString(args), i);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		} catch (InterruptedException e) {
//			log.info("Interrupted", e);
			Thread.currentThread().interrupt();
		}
	}
	
	@EventMapping
	public void StickerMessageEvent(MessageEvent<StickerMessageContent> event) {
		System.out.println(event.getMessage().getStickerId());
	}
	
	@EventMapping
	public void DefaultMessageEvent(Event event) {
		System.out.println("Event");
	}

}
