import shared
import SwiftUI

extension Stonk: Identifiable {}

extension Color {
	static let systemBackground = Color(.systemBackground)
	static let secondarySystemBackground = Color(.secondarySystemBackground)
	static let tertiarySystemBackground = Color(.tertiarySystemBackground)
}

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

	var body: some View {
		let sign = stonk.diffEuro < 0 ? "" : "+"
		let color: Color = stonk.diffEuro < 0 ? .red : .green
		VStack {
			HStack {
				Text("\(stonk.symbol)")
					.font(.footnote)
				Spacer()
				Text("\(stonk.count)x")
					.font(.footnote)
			}
			Text(stonk.name)
				.font(.title2)
				.multilineTextAlignment(.center)
			Divider()
			VStack {
				HStack {
					Text("Buy-in Price")
						.font(.headline)
						.bold()
						.frame(minWidth: 0, maxWidth: 150)
					Text("Buy-in Value")
						.font(.headline)
						.bold()
						.frame(minWidth: 0, maxWidth: 150)
				}
				HStack {
					Text("\(FormatKt.localNumber(number: stonk.buyinPrice, places: 3))€")
						.frame(minWidth: 0, maxWidth: 150)
					Text("\(FormatKt.localNumber(number: stonk.buyinValue, places: 2))€")
						.frame(minWidth: 0, maxWidth: 150)
				}
				HStack {
					Text("Price")
						.font(.headline)
						.bold()
						.frame(minWidth: 0, maxWidth: 150)
					Text("Value")
						.font(.headline)
						.bold()
						.frame(minWidth: 0, maxWidth: 150)
				}
				HStack {
					Text("\(FormatKt.localNumber(number: stonk.currentPrice, places: 3))€")
						.frame(minWidth: 0, maxWidth: 150)
					Text("\(FormatKt.localNumber(number: stonk.currentValue, places: 2))€")
						.frame(minWidth: 0, maxWidth: 150)
				}
			}
			Divider()
			HStack {
				Text("\(sign)\(FormatKt.localNumber(number: stonk.diffEuro, places: 2))€")
					.foregroundColor(color)
				Text(" (")
				Text("\(sign)\(FormatKt.localNumber(number: stonk.diffPercent, places: 2))%")
					.foregroundColor(color)
				Text(")")
			}
		}
			.padding(8)
			.background(
				RoundedRectangle(cornerRadius: 10)
					.foregroundColor(.secondarySystemBackground)
			)
	}
}

let AppWillEnterForegroundNotification = NotificationCenter.default.publisher(for: UIApplication.willEnterForegroundNotification)
let AppDidEnterBackgroundNotification = NotificationCenter.default.publisher(for: UIApplication.didEnterBackgroundNotification)

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

				let sign = result.portfolio.diffEuro < 0 ? "" : "+"
				let color = result.portfolio.diffEuro < 0 ? Color.red : Color.green
				VStack {
					HStack {
						Text("Buy-in Value")
							.font(.headline)
							.bold()
							.frame(minWidth: 0, maxWidth: 150)
						Text("Value")
							.font(.headline)
							.bold()
							.frame(minWidth: 0, maxWidth: 150)
					}
					HStack {
						Text("\(FormatKt.localNumber(number: result.portfolio.buyin, places: 2))€")
							.frame(minWidth: 0, maxWidth: 150)
						Text("\(FormatKt.localNumber(number: result.portfolio.value, places: 2))€")
							.frame(minWidth: 0, maxWidth: 150)
					}
				}
				HStack {
					Text("\(sign)\(FormatKt.localNumber(number: result.portfolio.diffEuro, places: 2))€")
						.foregroundColor(color)
					Text(" (")
					Text("\(sign)\(FormatKt.localNumber(number: result.portfolio.diffPercent, places: 2))%")
						.foregroundColor(color)
					Text(")")
				}

				Text(FormatKt.relativeTime(fromTimestamp: result.timestamp))

				ScrollView {
					LazyVGrid(columns: [GridItem()]) {
						ForEach(result.stonks) { stonk in
							StonkItem(stonk: stonk)
								.padding(.horizontal, 8)
						}
					}
				}
			}
		}
			.onAppear { viewModel.start() }
			.onReceive(AppWillEnterForegroundNotification) { _ in viewModel.start() }
			.onReceive(AppDidEnterBackgroundNotification) { _ in viewModel.stop() }
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
