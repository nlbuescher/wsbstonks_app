import shared
import SwiftUI

extension Stonk: Identifiable {}

class ContentViewModel: ObservableObject {
	enum State {
		case loading
		case loaded(StonksResult)
		case failed(Error)
	}

	private let api = Api()
	private var timer: Timer? = nil

	@Published private(set) var state: State = .loading

	func start() {
		if timer == nil {
			timer = Timer.scheduledTimer(withTimeInterval: 10, repeats: true) { _ in
				self.api.allStonks { [weak self] result, error in
					if let result = result {
						self?.state = .loaded(result)
					} else if let error = error {
						self?.state = .failed(error)
					}
				}
			}
			timer!.fire()
		}
	}

	func stop() {
		state = .loading
		timer?.invalidate()
		timer = nil
	}
}

struct StonkItem: View {
	let stonk: Stonk

	init(_ stonk: Stonk) { self.stonk = stonk }

	var body: some View {
		let sign = stonk.diffEuro < 0 ? "" : "+"
		let color = stonk.diffEuro < 0 ? Color.red : Color.green
		VStack {
			HStack {
				Text("\(stonk.symbol)").font(.footnote)
				Spacer()
				Text("\(stonk.count)x").font(.footnote)
			}
			Text(stonk.name).font(.title2)
			VStack {
				HStack {
					Text("Buy-in Price")
						.font(.headline).bold()
						.aspectRatio(contentMode: .fit)
					Text("Buy-in Value")
						.font(.headline).bold()
						.aspectRatio(contentMode: .fill)
				}
				HStack {
					Text("\(FormatKt.localNumber(number: stonk.buyinPrice, places: 3))€")
						.aspectRatio(contentMode: .fit)
					Text("\(FormatKt.localNumber(number: stonk.buyinValue, places: 2))€")
						.aspectRatio(contentMode: .fill)
				}
				HStack {
					Text("Price")
						.font(.headline).bold()
						.aspectRatio(contentMode: .fit)
					Text("Value")
						.font(.headline).bold()
						.aspectRatio(contentMode: .fill)
				}
				HStack {
					Text("\(FormatKt.localNumber(number: stonk.currentPrice, places: 3))€")
						.aspectRatio(contentMode: .fit)
					Text("\(FormatKt.localNumber(number: stonk.currentValue, places: 2))€")
						.aspectRatio(contentMode: .fill)
				}
				HStack {
					Text("\(sign)\(FormatKt.localNumber(number: stonk.diffEuro, places: 2))€")
						.foregroundColor(color)
					Text(" (")
					Text("\(sign)\(FormatKt.localNumber(number: stonk.diffPercent, places: 2))%")
						.foregroundColor(color)
					Text(")")
				}
			}.aspectRatio(contentMode: .fit)
		}.padding(4)
	}
}

let AppWillEnterForeground = NotificationCenter.default.publisher(for: UIApplication.willEnterForegroundNotification)
let AppDidEnterBackground = NotificationCenter.default.publisher(for: UIApplication.didEnterBackgroundNotification)

struct ContentView: View {
	@ObservedObject var viewModel = ContentViewModel()

	var body: some View {
		VStack {
			switch viewModel.state {
			case .loading:
				ProgressView()

			case .failed(let error):
				Text(error.localizedDescription)

			case .loaded(let result):
				Text("Summary")
					.font(.largeTitle)
					.bold()


				HStack {
					Text("Buy-in Value")
						.font(.headline).bold()
						.aspectRatio(contentMode: .fit)
					Text("Value")
						.font(.headline).bold()
						.aspectRatio(contentMode: .fill)
				}
				HStack {

				}

				Text(FormatKt.relativeTime(fromTimestamp: result.timestamp))

				List {
					ForEach(result.stonks) { stonk in
						StonkItem(stonk)
					}
				}
			}
		}
			.onAppear { viewModel.start() }
			.onReceive(AppWillEnterForeground) { _ in viewModel.start() }
			.onReceive(AppDidEnterBackground) { _ in viewModel.stop() }
	}
}

@main
struct WsbStonksApp: App {
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
